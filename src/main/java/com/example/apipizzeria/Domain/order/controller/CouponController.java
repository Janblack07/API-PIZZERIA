package com.example.apipizzeria.Domain.order.controller;

import com.example.apipizzeria.Domain.order.entity.Order;
import com.example.apipizzeria.Domain.order.service.OrderService;
import com.example.apipizzeria.Domain.user.entity.User;
import com.example.apipizzeria.Domain.user.repository.UserRepository;
import com.example.apipizzeria.common.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/me/coupon")
@PreAuthorize("hasAuthority('TYPE_CLIENT')")
public class CouponController {

    private final OrderService orders;
    private final UserRepository users;

    public CouponController(OrderService orders,UserRepository users) {
        this.orders = orders;
        this.users = users;
    }

    /** Extrae el userId del Authentication.details (claims) o, si no está, por email del principal. */
    private Long currentUserId(Authentication auth) {
        if (auth == null) throw new IllegalStateException("No authenticated user");
        // 1) intentar desde claims en details (seteado por JwtAuthFilter)
        Object details = auth.getDetails();
        if (details instanceof Map<?,?> map) {
            Object uid = map.get("uid");
            if (uid != null) {
                try { return Long.valueOf(String.valueOf(uid)); } catch (NumberFormatException ignored) {}
            }
            Object id = map.get("id"); // por si acaso
            if (id != null) {
                try { return Long.valueOf(String.valueOf(id)); } catch (NumberFormatException ignored) {}
            }
        }
        // 2) fallback: email en principal
        String email = auth.getName();
        if (email == null || email.isBlank()) throw new IllegalStateException("Cannot resolve user from token");
        User u = users.findByEmailIgnoreCase(email).orElseThrow(() -> new IllegalStateException("User not found: " + email));
        return u.getId();
    }

    @PostMapping
    @Operation(summary = "Aplicar cupón al carrito actual")
    public ApiResponse<Order> apply(Authentication auth, @RequestParam("code") String code) {
        return ApiResponse.ok(orders.applyCoupon(currentUserId(auth), code));
    }

    @DeleteMapping
    @Operation(summary = "Quitar cupón del carrito actual")
    public ApiResponse<Order> clear(Authentication auth) {
        return ApiResponse.ok(orders.applyCoupon(currentUserId(auth), ""));
    }
}
