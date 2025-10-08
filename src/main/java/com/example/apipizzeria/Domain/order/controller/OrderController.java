package com.example.apipizzeria.Domain.order.controller;

import com.example.apipizzeria.Domain.order.dto.*;
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
@RequestMapping("/api/me/cart")
@PreAuthorize("hasAuthority('TYPE_CLIENT')")
public class OrderController {

    private final OrderService orders;
    private final UserRepository users;

    public OrderController(OrderService orders, UserRepository users) {
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

    @GetMapping
    @Operation(summary = "Obtener carrito (orden DRAFT/NEW)")
    public ApiResponse<Order> getCart(Authentication auth) {
        return ApiResponse.ok(orders.getOrCreateDraft(currentUserId(auth)));
    }

    @PostMapping("/items")
    @Operation(summary = "Agregar ítem al carrito")
    public ApiResponse<Order> addItem(Authentication auth, @RequestBody AddItemRequest req) {
        return ApiResponse.ok(orders.addItem(currentUserId(auth), req.variantId(), req.quantity(), req.toppingIds()));
    }

    @PatchMapping("/items/{itemId}")
    @Operation(summary = "Actualizar cantidad de un ítem")
    public ApiResponse<Order> updateQty(Authentication auth, @PathVariable Long itemId, @RequestBody UpdateQtyRequest req) {
        return ApiResponse.ok(orders.updateQuantity(currentUserId(auth), itemId, req.quantity()));
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Eliminar un ítem del carrito")
    public ApiResponse<Order> removeItem(Authentication auth, @PathVariable Long itemId) {
        return ApiResponse.ok(orders.removeItem(currentUserId(auth), itemId));
    }

    @DeleteMapping("/items")
    @Operation(summary = "Vaciar carrito")
    public ApiResponse<Order> clear(Authentication auth) {
        return ApiResponse.ok(orders.clear(currentUserId(auth)));
    }

    @PostMapping("/coupon")
    @Operation(summary = "Aplicar cupón al carrito")
    public ApiResponse<Order> applyCoupon(Authentication auth, @RequestBody ApplyCouponRequest req) {
        return ApiResponse.ok(orders.applyCoupon(currentUserId(auth), req.code()));
    }

    @DeleteMapping("/coupon")
    @Operation(summary = "Quitar cupón del carrito")
    public ApiResponse<Order> removeCoupon(Authentication auth) {
        // tu CouponService pone 0 si el código es vacío/no válido
        return ApiResponse.ok(orders.applyCoupon(currentUserId(auth), ""));
    }

    @PostMapping("/tip")
    @Operation(summary = "Definir propina")
    public ApiResponse<Order> setTip(Authentication auth, @RequestBody TipRequest req) {
        return ApiResponse.ok(orders.setTip(currentUserId(auth), req.tipAmount()));
    }

    @PostMapping("/fee")
    @Operation(summary = "Definir tarifa de delivery")
    public ApiResponse<Order> setFee(Authentication auth, @RequestBody FeeRequest req) {
        return ApiResponse.ok(orders.setDeliveryFee(currentUserId(auth), req.deliveryFee()));
    }

    @PostMapping("/checkout")
    @Operation(summary = "Checkout: fija dirección y método de pago; avanza estado")
    public ApiResponse<Order> checkout(Authentication auth, @RequestBody CheckoutRequest req) {
        return ApiResponse.ok(orders.checkout(currentUserId(auth), req.addressId(), req.paymentMethod()));
    }
}