package com.example.apipizzeria.Domain.order.repository;

import com.example.apipizzeria.Domain.order.entity.Order;
import com.example.apipizzeria.common.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<Order> findByStatus(OrderStatus status);
}

