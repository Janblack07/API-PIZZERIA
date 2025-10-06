package com.example.apipizzeria.Domain.catalog.dto;

import com.example.apipizzeria.common.api.menu.dto.MediaDTO;
import com.example.apipizzeria.common.enums.Dough;
import com.example.apipizzeria.common.enums.Size;

import java.math.BigDecimal;

/** Variante (tamaño/masa/precio) con imagen opcional */
public record ProductVariantDTO(
        Long id,
        Size size,            // puede venir null si no aplica
        Dough dough,          // puede venir null si no aplica
        BigDecimal price,
        MediaDTO image        // de MediaAsset.secureUrl (puede ser null)
) {}
