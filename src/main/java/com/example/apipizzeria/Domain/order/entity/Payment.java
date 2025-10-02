package com.example.apipizzeria.Domain.order.entity;


import com.example.apipizzeria.common.BaseEntity;
import com.example.apipizzeria.common.enums.PaymentMethod;
import com.example.apipizzeria.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity @Table(name="payments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment extends BaseEntity {

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", unique = true)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;          // total cobrado

    @Column(length = 60)
    private String provider;            // "Stripe", "Mock", etc.


    @Column(length = 120)
    private String providerRef;         // id de transacción
}