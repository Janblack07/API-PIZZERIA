package com.example.apipizzeria.Domain.catalog.service.impl;

import com.example.apipizzeria.Configuration.media.entity.MediaAsset;
import com.example.apipizzeria.Configuration.media.repository.MediaAssetRepository;
import com.example.apipizzeria.Domain.catalog.dto.ProductDetailDTO;
import com.example.apipizzeria.Domain.catalog.dto.ProductListItemDTO;
import com.example.apipizzeria.Domain.catalog.dto.ProductUpsertRequest;
import com.example.apipizzeria.Domain.catalog.entity.*;
import com.example.apipizzeria.Domain.catalog.mapper.ProductMapper;
import com.example.apipizzeria.Domain.catalog.repository.*;
import com.example.apipizzeria.Domain.catalog.service.AdminProductService;
import com.example.apipizzeria.common.api.menu.dto.*;

import com.example.apipizzeria.common.enums.*;
import com.example.apipizzeria.common.exception.BadRequestException;
import com.example.apipizzeria.common.exception.NotFoundException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AdminProductServiceImpl implements AdminProductService {

    private final ProductRepository productRepo;
    private final ProductVariantRepository variantRepo;
    private final ProductAllowedToppingRepository patRepo;
    private final ComboItemRepository comboRepo;
    private final MediaAssetRepository mediaRepo;
    private final ToppingRepository toppingRepo; // asume que existe

    public AdminProductServiceImpl(ProductRepository productRepo,
                                   ProductVariantRepository variantRepo,
                                   ProductAllowedToppingRepository patRepo,
                                   ComboItemRepository comboRepo,
                                   MediaAssetRepository mediaRepo,
                                   ToppingRepository toppingRepo) {
        this.productRepo = productRepo;
        this.variantRepo = variantRepo;
        this.patRepo = patRepo;
        this.comboRepo = comboRepo;
        this.mediaRepo = mediaRepo;
        this.toppingRepo = toppingRepo;
    }

    @Override
    public ProductDetailDTO create(ProductUpsertRequest req) {
        Product p = new Product();
        applyUpsert(p, req);
        p.setActive(true);
        productRepo.save(p);
        return ProductMapper.toDetail(p);
    }

    @Override
    public ProductDetailDTO update(Long id, ProductUpsertRequest req) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        applyUpsert(p, req);
        return ProductMapper.toDetail(p);
    }

    @Override
    public void delete(Long id) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        p.setActive(false); // soft delete
    }

    @Override
    public ProductDetailDTO setActive(Long id, boolean active) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        p.setActive(active);
        return ProductMapper.toDetail(p);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailDTO get(Long id) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        return ProductMapper.toDetail(p);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductListItemDTO> list(ProductType type, String q, Boolean active, Pageable pageable) {
        Page<Product> page;
        boolean hasType = (type != null);
        boolean hasQ = (q != null && !q.isBlank());
        boolean hasActive = (active != null);

        if (!hasActive) { // default: admin ve todos
            if (!hasType && !hasQ) page = productRepo.findAll(pageable);
            else if (hasType && !hasQ) page = productRepo.findByType(type, pageable);
            else if (!hasType) page = productRepo.findByNameContainingIgnoreCase(q.trim(), pageable);
            else page = productRepo.findByTypeAndNameContainingIgnoreCase(type, q.trim(), pageable);
        } else if (active) {
            if (!hasType && !hasQ) page = productRepo.findByActiveTrue(pageable);
            else if (hasType && !hasQ) page = productRepo.findByActiveTrueAndType(type, pageable);
            else if (!hasType) page = productRepo.findByActiveTrueAndNameContainingIgnoreCase(q.trim(), pageable);
            else page = productRepo.findByActiveTrueAndTypeAndNameContainingIgnoreCase(type, q.trim(), pageable);
        } else {
            // inactivos: filtra en memoria (pocos casos normalmente) o crea queries específicas si prefieres
            page = productRepo.findAll(pageable)
                    .map(p -> p) // no-op
                    .map(p -> p) ;
            page = new PageImpl<>(
                    page.getContent().stream().filter(p -> !p.isActive()).toList(),
                    pageable,
                    page.getTotalElements() // si quieres exactitud crea repos dedicados
            );
        }

        return page.map(ProductMapper::toListItem);
    }

    /* ================= helpers ================= */

    private void applyUpsert(Product p, ProductUpsertRequest req) {
        p.setName(req.name());
        p.setDescription(req.description());
        p.setType(req.type());
        p.setBasePrice(req.basePrice());
        p.setCustomizable(req.customizable());

        // featured image
        if (req.featuredImageId() != null) {
            MediaAsset mi = mediaRepo.findById(req.featuredImageId())
                    .orElseThrow(() -> new NotFoundException("Imagen principal no existe"));
            p.setFeaturedImage(mi);
        } else {
            p.setFeaturedImage(null);
        }

        // gallery
        p.getGallery().clear();
        if (req.galleryImageIds() != null && !req.galleryImageIds().isEmpty()) {
            List<MediaAsset> imgs = mediaRepo.findAllById(req.galleryImageIds());
            if (imgs.size() != req.galleryImageIds().size()) {
                throw new BadRequestException("Alguna imagen de la galería no existe");
            }
            p.getGallery().addAll(imgs);
        }

        // variants (reemplazo total)
        p.getVariants().clear();
        if (req.variants() != null && !req.variants().isEmpty()) {
            for (var v : req.variants()) {
                ProductVariant pv = new ProductVariant();
                pv.setProduct(p);
                pv.setPrice(v.price());
                pv.setSize(v.size() != null ? Size.valueOf(v.size()) : null);
                pv.setDough(v.dough() != null ? Dough.valueOf(v.dough()) : null);
                if (v.imageId() != null) {
                    MediaAsset vi = mediaRepo.findById(v.imageId())
                            .orElseThrow(() -> new NotFoundException("Imagen de variante no existe"));
                    pv.setImage(vi);
                }
                p.getVariants().add(pv);
            }
        }

        // allowed toppings
        // limpiamos y recreamos
        // (si el producto no es pizza/customizable, lo borramos)
        if (req.allowedToppings() != null && !req.allowedToppings().isEmpty()) {
            if (p.getType() != ProductType.PIZZA && !p.isCustomizable()) {
                throw new BadRequestException("Toppings solo aplican a pizzas/customizables");
            }
        }
        // para borrar antiguos:
        // busca existentes y elimínalos (orphanRemoval en entity
        // ProductAllowedTopping gestiona al ser dueño Product? aquí es entidad separada)
        // estrategia simple: borrar por productId y volver a crear
        // si no tienes query especial, al ser relación sin mapeo bidireccional directo
        // en Product, se recrean:
        // Puedes añadir un repo method deleteByProductId(productId) si deseas.
        // Aquí, por simplicidad, borraremos por JPA cascade si has mapeado desde Product
        // (si no, recrea manualmente):
        // -> Suponiendo que NO está mapeado desde Product, recreamos manualmente:

        // Primero elimina existentes
        // (si quieres performance, crea en repo: void deleteByProduct(Product p);)
        patRepo.deleteAll(
                patRepo.findAll().stream().filter(x -> x.getProduct()!=null && x.getProduct().getId()!=null
                        && Objects.equals(x.getProduct().getId(), p.getId())).toList()
        );
        if (req.allowedToppings() != null) {
            for (var at : req.allowedToppings()) {
                Topping t = toppingRepo.findById(at.toppingId())
                        .orElseThrow(() -> new NotFoundException("Topping no existe"));
                ProductAllowedTopping pat = new ProductAllowedTopping();
                pat.setProduct(p);
                pat.setTopping(t);
                pat.setFreeAllowance(at.freeAllowance());
                patRepo.save(pat);
            }
        }

        // combo items (solo si type=COMBO)
        comboRepo.deleteAll(
                comboRepo.findAll().stream().filter(x -> x.getComboProduct()!=null && x.getComboProduct().getId()!=null
                        && Objects.equals(x.getComboProduct().getId(), p.getId())).toList()
        );
        if (p.getType() == ProductType.COMBO) {
            if (req.comboItems() == null || req.comboItems().isEmpty()) {
                throw new BadRequestException("Un combo debe tener al menos un ítem");
            }
            for (var ci : req.comboItems()) {
                Product child = productRepo.findById(ci.childProductId())
                        .orElseThrow(() -> new NotFoundException("Producto hijo no existe"));
                ComboItem item = new ComboItem();
                item.setComboProduct(p);
                item.setChildProduct(child);
                item.setQuantity(ci.quantity());
                comboRepo.save(item);
            }
        }
    }
}