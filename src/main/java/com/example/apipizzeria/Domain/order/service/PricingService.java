package com.example.apipizzeria.Domain.order.service;

import com.example.apipizzeria.Domain.order.entity.Order;


public interface PricingService {
    void recalc(Order order);     // precio topping
}
