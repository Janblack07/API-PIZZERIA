package com.example.apipizzeria.Domain.order.service.impl;

import com.example.apipizzeria.Domain.order.entity.Order;

import com.example.apipizzeria.Domain.order.service.CouponService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CouponServiceImpl implements CouponService {

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    @Override
    public void applyCoupon(Order order, String code) {
        if (order == null) return;

        // Si no hay items, descuento = 0
        if (order.getItemsTotal() == null || order.getItemsTotal().compareTo(ZERO) <= 0) {
            order.setDiscountTotal(ZERO);
            return;
        }

        // Regla temporal: "DEMO10" => 10% del itemsTotal, tope 15.00
        if (code != null && code.trim().equalsIgnoreCase("DEMO10")) {
            BigDecimal discount = order.getItemsTotal().multiply(new BigDecimal("0.10"));
            BigDecimal cap = new BigDecimal("15.00");
            if (discount.compareTo(cap) > 0) discount = cap;
            order.setDiscountTotal(discount);
        } else {
            // Sin cupón válido => 0
            order.setDiscountTotal(ZERO);
        }
    }
}