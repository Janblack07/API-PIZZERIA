package com.example.apipizzeria.Domain.order.repository;

import com.example.apipizzeria.Domain.order.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> { }