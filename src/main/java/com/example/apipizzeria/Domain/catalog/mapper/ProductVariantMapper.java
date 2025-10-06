package com.example.apipizzeria.Domain.catalog.mapper;

import com.example.apipizzeria.Configuration.media.mapper.MediaMapper;
import com.example.apipizzeria.Domain.catalog.dto.ProductVariantDTO;
import com.example.apipizzeria.Domain.catalog.entity.ProductVariant;
import com.example.apipizzeria.common.api.menu.dto.MediaDTO;


public final class ProductVariantMapper {
    private ProductVariantMapper() {}

    public static ProductVariantDTO toDTO(ProductVariant v) {
        if (v == null) return null;
        MediaDTO image = MediaMapper.toDTO(v.getImage());
        return new ProductVariantDTO(
                v.getId(),
                v.getSize(),
                v.getDough(),
                v.getPrice(),
                image
        );
    }
}