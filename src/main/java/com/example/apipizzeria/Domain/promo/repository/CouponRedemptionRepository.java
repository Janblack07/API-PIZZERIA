package com.example.apipizzeria.Domain.promo.repository;


import com.example.apipizzeria.Domain.promo.entity.CouponRedemption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRedemptionRepository extends JpaRepository<CouponRedemption, Long> { }