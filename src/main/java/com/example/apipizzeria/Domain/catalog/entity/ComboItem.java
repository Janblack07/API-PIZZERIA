package com.example.apipizzeria.Domain.catalog.entity;


import com.example.apipizzeria.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="combo_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ComboItem extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="combo_product_id")
    private Product comboProduct;      // debe tener type = COMBO

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="child_product_id")
    private Product childProduct;

    @Column(nullable = false)
    private Integer quantity;
}