package com.example.apipizzeria.Domain.catalog.entity;

import com.example.apipizzeria.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity @Table(name="coupons",
        indexes = @Index(name="ix_coupons_code", columnList = "code", unique = true))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Coupon extends BaseEntity {

    @Column(nullable = false, length = 40, unique = true)
    private String code;

    // cantidad fija o % (usa solo uno)
    @Column(precision = 10, scale = 2)
    private BigDecimal amountOff;

    @Column(precision = 5, scale = 2)
    private BigDecimal percentOff;

    @Column(precision = 10, scale = 2)
    private BigDecimal minOrderTotal;

    private Instant startsAt;
    private Instant expiresAt;

    private Integer maxRedemptions;   // total permitido
    private Integer perUserLimit;     // por usuario
    private boolean active = true;
}