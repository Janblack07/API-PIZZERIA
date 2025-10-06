package com.example.apipizzeria.common.api.menu.service.impl;

import com.example.apipizzeria.Domain.catalog.dto.ProductDetailDTO;
import com.example.apipizzeria.Domain.catalog.dto.ProductListItemDTO;
import com.example.apipizzeria.Domain.catalog.entity.Product;
import com.example.apipizzeria.Domain.catalog.mapper.ProductMapper;
import com.example.apipizzeria.Domain.catalog.repository.ProductRepository;
import com.example.apipizzeria.common.api.menu.service.MenuService;
import com.example.apipizzeria.common.enums.ProductType;
import com.example.apipizzeria.common.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {

    private final ProductRepository productRepo;

    public MenuServiceImpl(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Page<ProductListItemDTO> listProducts(ProductType type, String q, Pageable pageable) {
        Page<Product> page;

        boolean hasType = (type != null);
        boolean hasQ = (q != null && !q.isBlank());
        String query = hasQ ? q.trim() : null;

        if (!hasType && !hasQ) {
            page = productRepo.findByActiveTrue(pageable);
        } else if (hasType && !hasQ) {
            page = productRepo.findByActiveTrueAndType(type, pageable);
        } else if (!hasType) {
            page = productRepo.findByActiveTrueAndNameContainingIgnoreCase(query, pageable);
        } else {
            page = productRepo.findByActiveTrueAndTypeAndNameContainingIgnoreCase(type, query, pageable);
        }

        return page.map(ProductMapper::toListItem);
    }

    @Override
    public ProductDetailDTO getProduct(Long productId) {
        Product p = productRepo.findByIdAndActiveTrue(productId)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        return ProductMapper.toDetail(p);
    }
}
