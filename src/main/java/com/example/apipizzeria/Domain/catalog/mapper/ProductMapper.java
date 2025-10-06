package com.example.apipizzeria.Domain.catalog.mapper;

import com.example.apipizzeria.Configuration.media.mapper.MediaMapper;
import com.example.apipizzeria.Domain.catalog.dto.ProductDetailDTO;
import com.example.apipizzeria.Domain.catalog.dto.ProductListItemDTO;
import com.example.apipizzeria.Domain.catalog.dto.ProductVariantDTO;
import com.example.apipizzeria.Domain.catalog.entity.Product;
import com.example.apipizzeria.Domain.catalog.entity.ProductVariant;
import com.example.apipizzeria.common.api.menu.dto.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class ProductMapper {
    private ProductMapper() {}

    /** Mapea a DTO de listado (ligero) */
    public static ProductListItemDTO toListItem(Product p) {
        if (p == null) return null;

        var featured = MediaMapper.toDTO(p.getFeaturedImage());
        var variants = mapVariantsSorted(p);

        return new ProductListItemDTO(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getType(),
                p.getBasePrice(),
                p.isCustomizable(),
                featured,
                variants
        );
    }

    /** Mapea a DTO de detalle (completo) */
    public static ProductDetailDTO toDetail(Product p) {
        if (p == null) return null;

        var featured = MediaMapper.toDTO(p.getFeaturedImage());
        var gallery  = MediaMapper.toDTOList(p.getGallery());
        var variants = mapVariantsSorted(p);

        return new ProductDetailDTO(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getType(),
                p.getBasePrice(),
                p.isActive(),
                p.isCustomizable(),
                featured,
                gallery,
                variants
        );
    }

    private static List<ProductVariantDTO> mapVariantsSorted(Product p) {
        if (p.getVariants() == null || p.getVariants().isEmpty()) return List.of();

        // Orden estable: primero size (nulls last), luego dough (nulls last), luego id
        Comparator<ProductVariant> bySize =
                Comparator.comparing(v -> v.getSize(), Comparator.nullsLast(Comparator.naturalOrder()));
        Comparator<ProductVariant> byDough =
                Comparator.comparing(v -> v.getDough(), Comparator.nullsLast(Comparator.naturalOrder()));
        Comparator<ProductVariant> byId =
                Comparator.comparing(ProductVariant::getId, Comparator.nullsLast(Comparator.naturalOrder()));

        return p.getVariants().stream()
                .filter(Objects::nonNull)
                .sorted(bySize.thenComparing(byDough).thenComparing(byId))
                .map(ProductVariantMapper::toDTO)
                .toList();
    }
}