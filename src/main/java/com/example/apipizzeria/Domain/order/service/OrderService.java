package com.example.apipizzeria.Domain.order.service;

import com.example.apipizzeria.Domain.order.entity.Order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    Order getOrCreateDraft(Long customerId);

    Order addItem(Long customerId, Long variantId, Integer quantity, List<Long> toppingIds);
    Order updateQuantity(Long customerId, Long orderItemId, Integer quantity);
    Order removeItem(Long customerId, Long orderItemId);
    Order clear(Long customerId);

    Order applyCoupon(Long customerId, String code);
    Order setTip(Long customerId, BigDecimal tipAmount);
    Order setDeliveryFee(Long customerId, BigDecimal fee);

    Order checkout(Long customerId, Long addressId, String paymentMethod);
}
