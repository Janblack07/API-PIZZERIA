package com.example.apipizzeria.Domain.order.repository;


import com.example.apipizzeria.Domain.order.entity.OrderStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, Long> { }
