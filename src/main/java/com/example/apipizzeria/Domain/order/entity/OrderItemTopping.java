package com.example.apipizzeria.Domain.order.entity;


import com.example.apipizzeria.Domain.catalog.entity.Topping;
import com.example.apipizzeria.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity @Table(name="order_item_toppings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItemTopping extends BaseEntity {

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="order_item_id")
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="topping_id")
    private Topping topping;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;           // precio al momento
}
