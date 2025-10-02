package com.example.apipizzeria.Domain.order.entity;


import com.example.apipizzeria.Domain.catalog.entity.Product;
import com.example.apipizzeria.Domain.catalog.entity.ProductVariant;
import com.example.apipizzeria.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity @Table(name="order_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem extends BaseEntity {

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    // Snapshot de producto/variante (id + nombre/precio por si cambian en el tiempo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="variant_id")
    private ProductVariant variant;     // nullable si no aplica

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;       // precio base (o de variante) al momento

    @Column(precision = 10, scale = 2)
    private BigDecimal extrasTotal;     // suma de toppings/extra

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal lineTotal;       // (unitPrice + extras) * qty

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItemTopping> toppings = new HashSet<>();
}