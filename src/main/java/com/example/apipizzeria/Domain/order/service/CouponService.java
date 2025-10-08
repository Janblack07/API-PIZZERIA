package com.example.apipizzeria.Domain.order.service;

import com.example.apipizzeria.Domain.order.entity.Order;

public interface CouponService {
    void applyCoupon(Order order, String code);
}
