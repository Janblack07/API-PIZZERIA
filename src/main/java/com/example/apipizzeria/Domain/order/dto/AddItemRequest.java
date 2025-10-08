package com.example.apipizzeria.Domain.order.dto;

import java.util.List;

public record AddItemRequest(Long variantId, Integer quantity, List<Long> toppingIds) {
}
