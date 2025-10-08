package com.example.apipizzeria.Domain.order.repository;

import com.example.apipizzeria.Domain.order.entity.OrderItemTopping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemToppingRepository extends JpaRepository<OrderItemTopping, Long> {
    void deleteByOrderItem_Id(Long orderItemId);
}