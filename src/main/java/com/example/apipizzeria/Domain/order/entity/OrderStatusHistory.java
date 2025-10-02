package com.example.apipizzeria.Domain.order.entity;

import com.example.apipizzeria.Domain.user.entity.User;
import com.example.apipizzeria.common.BaseEntity;
import com.example.apipizzeria.common.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="order_status_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderStatusHistory extends BaseEntity {

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="changed_by")      // staff o sistema
    private User changedBy;

    @Column(length = 200)
    private String notes;
}


