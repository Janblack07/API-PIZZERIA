package com.example.apipizzeria.Domain.order.service.impl;

import com.example.apipizzeria.Domain.catalog.entity.Product;
import com.example.apipizzeria.Domain.catalog.entity.ProductVariant;
import com.example.apipizzeria.Domain.catalog.entity.Topping;
import com.example.apipizzeria.Domain.catalog.repository.ProductVariantRepository;
import com.example.apipizzeria.Domain.catalog.repository.ToppingRepository;
import com.example.apipizzeria.Domain.order.entity.DeliveryAddress;
import com.example.apipizzeria.Domain.order.entity.Order;
import com.example.apipizzeria.Domain.order.entity.OrderItem;
import com.example.apipizzeria.Domain.order.entity.OrderItemTopping;
import com.example.apipizzeria.Domain.order.repository.OrderItemRepository;
import com.example.apipizzeria.Domain.order.repository.OrderItemToppingRepository;
import com.example.apipizzeria.Domain.order.repository.OrderRepository;
import com.example.apipizzeria.Domain.order.service.CouponService;
import com.example.apipizzeria.Domain.order.service.OrderService;
import com.example.apipizzeria.Domain.order.service.PricingService;

import com.example.apipizzeria.Domain.user.entity.Address;
import com.example.apipizzeria.Domain.user.entity.User;
import com.example.apipizzeria.Domain.user.repository.AddressRepository;
import com.example.apipizzeria.Domain.user.repository.UserRepository;
import com.example.apipizzeria.common.enums.OrderStatus;
import com.example.apipizzeria.common.enums.PaymentMethod;
import com.example.apipizzeria.common.enums.PaymentStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    private final OrderRepository orders;
    private final OrderItemRepository items;
    private final OrderItemToppingRepository itemToppings;
    private final ProductVariantRepository variants;
    private final ToppingRepository toppings;
    private final AddressRepository addresses;
    private final UserRepository users;
    private final PricingService pricing;
    private final CouponService coupons;

    public OrderServiceImpl(OrderRepository orders,
                            OrderItemRepository items,
                            OrderItemToppingRepository itemToppings,
                            ProductVariantRepository variants,
                            ToppingRepository toppings,
                            AddressRepository addresses,
                            UserRepository users,
                            PricingService pricing,
                            CouponService coupons) {
        this.orders = orders;
        this.items = items;
        this.itemToppings = itemToppings;
        this.variants = variants;
        this.toppings = toppings;
        this.addresses = addresses;
        this.users = users;
        this.pricing = pricing;
        this.coupons = coupons;
    }

    /** Carrito = DRAFT. Aquí usamos OrderStatus.NEW como draft.
     *  Si tu enum usa DRAFT, cambia NEW por DRAFT en los dos lugares que aparece. */
    private Order requireDraft(Long customerId) {
        return orders.findFirstByCustomer_IdAndStatusOrderByCreatedAtAsc(customerId, OrderStatus.NEW)
                .orElseGet(() -> {
                    User customer = users.findById(customerId).orElseThrow();

                    Order o = new Order();
                    o.setCustomer(customer);
                    o.setStatus(OrderStatus.NEW);              // <- DRAFT si tu enum lo tiene
                    o.setPaymentStatus(PaymentStatus.PENDING); // aún no pagado
                    o.setItems(new HashSet<>());

                    // Totales iniciales
                    o.setItemsTotal(ZERO);
                    o.setDiscountTotal(ZERO);
                    o.setDeliveryFee(ZERO);
                    o.setTipAmount(ZERO);
                    o.setGrandTotal(ZERO);

                    return orders.save(o);
                });
    }

    @Override
    public Order getOrCreateDraft(Long customerId) {
        return requireDraft(customerId);
    }

    @Override
    @Transactional
    public Order addItem(Long customerId, Long variantId, Integer quantity, List<Long> toppingIds) {
        if (quantity == null || quantity <= 0) throw new IllegalArgumentException("Cantidad inválida");

        Order order = requireDraft(customerId);

        ProductVariant variant = variants.findById(variantId).orElseThrow();
        Product product = variant.getProduct();

        OrderItem it = new OrderItem();
        it.setOrder(order);
        it.setProduct(product);
        it.setVariant(variant);
        it.setQuantity(quantity);

        // Snapshot del precio base (variante o base del producto)
        BigDecimal base = variant.getPrice() != null ? variant.getPrice()
                : (product != null && product.getBasePrice() != null ? product.getBasePrice() : ZERO);
        it.setUnitPrice(base);

        // Toppings (snapshot de precio en OrderItemTopping.price)
        Set<OrderItemTopping> tops = new HashSet<>();
        if (toppingIds != null) {
            for (Long tid : toppingIds) {
                Topping t = toppings.findById(tid).orElseThrow();
                OrderItemTopping ot = new OrderItemTopping();
                ot.setOrderItem(it);
                ot.setTopping(t);
                ot.setPrice(t.getPrice() != null ? t.getPrice() : ZERO);
                tops.add(ot);
            }
        }
        it.setToppings(tops);

        // Agregar al pedido y recalcular
        order.getItems().add(it);
        pricing.recalc(order);

        // Persistencia
        items.save(it);      // explícito; cascada del order también lo haría
        return orders.save(order);
    }

    @Override
    @Transactional
    public Order updateQuantity(Long customerId, Long orderItemId, Integer quantity) {
        if (quantity == null || quantity <= 0) throw new IllegalArgumentException("Cantidad inválida");

        Order order = requireDraft(customerId);
        OrderItem it = items.findById(orderItemId).orElseThrow();

        if (!it.getOrder().getId().equals(order.getId()))
            throw new IllegalArgumentException("El ítem no pertenece a tu carrito");

        it.setQuantity(quantity);

        pricing.recalc(order);
        items.save(it);
        return orders.save(order);
    }

    @Override
    @Transactional
    public Order removeItem(Long customerId, Long orderItemId) {
        Order order = requireDraft(customerId);
        OrderItem it = items.findById(orderItemId).orElseThrow();

        if (!it.getOrder().getId().equals(order.getId()))
            throw new IllegalArgumentException("El ítem no pertenece a tu carrito");

        itemToppings.deleteByOrderItem_Id(it.getId());
        order.getItems().remove(it);
        items.delete(it);

        pricing.recalc(order);
        return orders.save(order);
    }

    @Override
    @Transactional
    public Order clear(Long customerId) {
        Order order = requireDraft(customerId);

        for (OrderItem it : order.getItems()) {
            itemToppings.deleteByOrderItem_Id(it.getId());
        }
        items.deleteAll(order.getItems());
        order.getItems().clear();

        pricing.recalc(order);
        return orders.save(order);
    }

    @Override
    @Transactional
    public Order applyCoupon(Long customerId, String code) {
        Order order = requireDraft(customerId);
        pricing.recalc(order);         // asegura itemsTotal actualizado
        coupons.applyCoupon(order, code); // setea discountTotal dentro del Order
        pricing.recalc(order);         // recalcula grandTotal con el descuento
        return orders.save(order);
    }

    @Override
    @Transactional
    public Order setTip(Long customerId, BigDecimal tipAmount) {
        Order order = requireDraft(customerId);
        order.setTipAmount(tipAmount == null ? ZERO : tipAmount.max(ZERO));
        pricing.recalc(order);
        return orders.save(order);
    }

    @Override
    @Transactional
    public Order setDeliveryFee(Long customerId, BigDecimal fee) {
        Order order = requireDraft(customerId);
        order.setDeliveryFee(fee == null ? ZERO : fee.max(ZERO));
        pricing.recalc(order);
        return orders.save(order);
    }

    @Override
    @Transactional
    public Order checkout(Long customerId, Long addressId, String paymentMethod) {
        Order order = requireDraft(customerId);

        // --- Snapshot de Address (tolerante a nombres distintos) ---
        Address addr = addresses.findById(addressId).orElseThrow();

        String recipientName = tryGetString(addr, "getRecipientName", "getFullName", "getName", "getLabel", "getAlias");
        String phone         = tryGetString(addr, "getPhone", "getPhoneNumber", "getCellphone", "getMobile");
        String streetLine    = tryGetString(addr, "getStreetLine", "getLine1", "getAddressLine1", "getStreet", "getAddress");
        Double lat           = tryGetDouble(addr, "getLat", "getLatitude", "getY");
        Double lng           = tryGetDouble(addr, "getLng", "getLongitude", "getX");
        String notes         = tryGetString(addr, "getNotes", "getReference", "getReferences", "getDescription", "getDetail");

        DeliveryAddress snap = DeliveryAddress.builder()
                .recipientName(recipientName)
                .phone(phone)
                .streetLine(streetLine)
                .lat(lat)
                .lng(lng)
                .notes(notes)
                .build();

        order.setDeliveryAddress(snap);

        // Método de pago y estado de pago
        order.setPaymentMethod(PaymentMethod.valueOf(paymentMethod)); // "CASH", "CARD", etc.
        order.setPaymentStatus(PaymentStatus.PENDING);

        // Totales finales y paso a flujo KDS
        pricing.recalc(order);
        order.setStatus(OrderStatus.PREPARING); // o PLACED si prefieres ese estado

        return orders.save(order);
    }

    // ===== Helpers de reflexión para evitar acoplarse a los nombres exactos de Address =====
    private static String tryGetString(Object obj, String... methods) {
        if (obj == null) return null;
        for (String m : methods) {
            try {
                var mm = obj.getClass().getMethod(m);
                Object v = mm.invoke(obj);
                if (v != null) return String.valueOf(v);
            } catch (Exception ignored) {}
        }
        return null;
    }

    private static Double tryGetDouble(Object obj, String... methods) {
        if (obj == null) return null;
        for (String m : methods) {
            try {
                var mm = obj.getClass().getMethod(m);
                Object v = mm.invoke(obj);
                if (v instanceof Number n) return n.doubleValue();
                if (v != null) return Double.valueOf(String.valueOf(v));
            } catch (Exception ignored) {}
        }
        return null;
    }
}