package com.example.apipizzeria.Domain.catalog.dto;

import com.example.apipizzeria.common.enums.ProductType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record ProductUpsertRequest(
        @NotBlank @Size(max = 160) String name,
        @Size(max = 400) String description,
        @NotNull ProductType type,
        @PositiveOrZero BigDecimal basePrice,          // opcional si solo hay variantes
        boolean customizable,
        Long featuredImageId,                         // MediaAsset.id
        List<Long> galleryImageIds,                   // MediaAsset.id[]

        // Variantes (opcional)
        List<VariantReq> variants,

        // Para pizzas armables: toppings permitidos (opcional)
        List<AllowedToppingReq> allowedToppings,

        // Para type=COMBO: ítems del combo (opcional)
        List<ComboItemReq> comboItems
) {
    public record VariantReq(
            String size,       // enum name de Size, o null
            String dough,      // enum name de Dough, o null
            @NotNull @Positive BigDecimal price,
            Long imageId       // MediaAsset.id (opcional)
    ) { }

    public record AllowedToppingReq(
            @NotNull Long toppingId,
            Integer freeAllowance // puede ser null
    ) { }

    public record ComboItemReq(
            @NotNull Long childProductId,
            @NotNull @Positive Integer quantity
    ) { }
}
