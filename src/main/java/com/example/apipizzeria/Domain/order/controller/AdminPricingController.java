package com.example.apipizzeria.Domain.order.controller;

import com.example.apipizzeria.Domain.order.entity.Order;
import com.example.apipizzeria.Domain.order.repository.OrderRepository;
import com.example.apipizzeria.Domain.order.service.PricingService;
import com.example.apipizzeria.common.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/pricing")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminPricingController {

    private final OrderRepository orders;
    private final PricingService pricing;

    public AdminPricingController(OrderRepository orders, PricingService pricing) {
        this.orders = orders;
        this.pricing = pricing;
    }

    @PostMapping("/orders/{orderId}/recalc")
    @Operation(summary = "Recalcular totales de una orden")
    public ApiResponse<Order> recalc(@PathVariable Long orderId) {
        Order o = orders.findById(orderId).orElseThrow();
        pricing.recalc(o);
        return ApiResponse.ok(orders.save(o));
    }

    @GetMapping("/orders/{orderId}/totals")
    @Operation(summary = "Obtener totales actuales de una orden")
    public ApiResponse<OrderTotalsResponse> totals(@PathVariable Long orderId) {
        Order o = orders.findById(orderId).orElseThrow();
        return ApiResponse.ok(new OrderTotalsResponse(
                o.getItemsTotal(),
                o.getDiscountTotal(),
                o.getDeliveryFee(),
                o.getTipAmount(),
                o.getGrandTotal()
        ));
    }

    public record OrderTotalsResponse(
            java.math.BigDecimal itemsTotal,
            java.math.BigDecimal discountTotal,
            java.math.BigDecimal deliveryFee,
            java.math.BigDecimal tipAmount,
            java.math.BigDecimal grandTotal
    ) {}
}