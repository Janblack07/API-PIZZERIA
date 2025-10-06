package com.example.apipizzeria.Domain.catalog.dto;

import com.example.apipizzeria.common.api.menu.dto.MediaDTO;
import com.example.apipizzeria.common.enums.ProductType;

import java.math.BigDecimal;
import java.util.List;

/** Detalle completo para la ficha del producto */
public record ProductDetailDTO(
        Long id,
        String name,
        String description,
        ProductType type,
        BigDecimal basePrice,
        boolean active,
        boolean customizable,
        MediaDTO featuredImage,        // de Product.featuredImage
        List<MediaDTO> gallery,        // de Product.gallery
        List<ProductVariantDTO> variants
) {}