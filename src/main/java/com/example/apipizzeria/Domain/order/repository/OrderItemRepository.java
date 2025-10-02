package com.example.apipizzeria.Domain.order.repository;

import com.example.apipizzeria.Domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> { }
