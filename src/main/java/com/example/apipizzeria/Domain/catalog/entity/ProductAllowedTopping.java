package com.example.apipizzeria.Domain.catalog.entity;

import com.example.apipizzeria.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="product_allowed_toppings",
        uniqueConstraints = @UniqueConstraint(name="ux_product_topping", columnNames = {"product_id","topping_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductAllowedTopping extends BaseEntity {

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="topping_id")
    private Topping topping;

    // p.ej. gratis hasta N toppings
    private Integer freeAllowance; // nullable
}