package com.example.apipizzeria.Domain.catalog.entity;


import com.example.apipizzeria.Domain.user.entity.User;
import com.example.apipizzeria.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="coupon_redemptions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CouponRedemption extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="coupon_id")
    private Coupon coupon;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", unique = true)
    private Order order;               // canje aplicado a un pedido
}
