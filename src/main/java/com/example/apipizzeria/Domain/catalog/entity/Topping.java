package com.example.apipizzeria.Domain.catalog.entity;

import com.example.apipizzeria.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity @Table(name="toppings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Topping extends BaseEntity {

    @Column(nullable = false, length = 80, unique = true)
    private String name;                // "Extra Queso"

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    private boolean active = true;
}