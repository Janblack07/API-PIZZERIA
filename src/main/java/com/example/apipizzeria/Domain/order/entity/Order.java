package com.example.apipizzeria.Domain.order.entity;


import com.example.apipizzeria.Domain.user.entity.User;
import com.example.apipizzeria.common.BaseEntity;
import com.example.apipizzeria.common.enums.OrderStatus;
import com.example.apipizzeria.common.enums.PaymentMethod;
import com.example.apipizzeria.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

@Entity @Table(name="orders",
        indexes = @Index(name="ix_orders_customer", columnList = "customer_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Order extends BaseEntity {

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id")
    private User customer;


    // Snapshot de entrega (se guarda copia por si el usuario edita su address luego)
    @Embedded
    private DeliveryAddress deliveryAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    // Totales
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal itemsTotal;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountTotal;

    @Column(precision = 10, scale = 2)
    private BigDecimal deliveryFee;

    @Column(precision = 10, scale = 2)
    private BigDecimal tipAmount;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal grandTotal;

    // Asignación de repartidor (si aplica)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="driver_id")
    private User driver; // STAFF con rol DRIVER

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> items = new HashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderStatusHistory> statusHistory = new ArrayList<>();
}