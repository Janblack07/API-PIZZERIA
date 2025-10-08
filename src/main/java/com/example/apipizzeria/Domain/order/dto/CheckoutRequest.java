package com.example.apipizzeria.Domain.order.dto;

public record CheckoutRequest(Long addressId, String paymentMethod) {
}
