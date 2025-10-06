package com.example.apipizzeria.Domain.catalog.dto;

import com.example.apipizzeria.common.api.menu.dto.MediaDTO;
import com.example.apipizzeria.common.enums.ProductType;

import java.math.BigDecimal;
import java.util.List;

/** Item para listados de menú (ligero) */
public record ProductListItemDTO(
        Long id,
        String name,
        String description,
        ProductType type,
        BigDecimal basePrice,     // usado cuando no hay variantes
        boolean customizable,     // builder habilitado
        MediaDTO featuredImage,   // imagen principal (puede ser null)
        List<ProductVariantDTO> variants
) {}
