package com.example.apipizzeria.Domain.order.service.impl;

import com.example.apipizzeria.Domain.order.entity.Order;
import com.example.apipizzeria.Domain.order.entity.OrderItem;
import com.example.apipizzeria.Domain.order.entity.OrderItemTopping;
import com.example.apipizzeria.Domain.order.service.PricingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PricingServiceImpl implements PricingService {

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    @Override
    public void recalc(Order o) {
        BigDecimal itemsTotal = ZERO;

        if (o.getItems() != null) {
            for (OrderItem it : o.getItems()) {

                // Asegurar unitPrice (si no vino seteado, lo tomamos de la variante)
                BigDecimal unit = it.getUnitPrice();
                if (unit == null) {
                    if (it.getVariant() != null && it.getVariant().getPrice() != null) {
                        unit = it.getVariant().getPrice();
                    } else if (it.getProduct() != null && it.getProduct().getBasePrice() != null) {
                        // opcional: si manejas un precio en el Product
                        unit = it.getProduct().getBasePrice();
                    } else {
                        unit = ZERO;
                    }
                    it.setUnitPrice(unit);
                }

                // Calcular extrasTotal: suma de los toppings (usando snapshot price)
                BigDecimal extras = ZERO;
                if (it.getToppings() != null) {
                    for (OrderItemTopping ot : it.getToppings()) {
                        BigDecimal p = ot.getPrice();
                        if (p == null) {
                            // snapshot: si no se guardó, tomar precio actual del topping
                            if (ot.getTopping() != null && ot.getTopping().getPrice() != null) {
                                p = ot.getTopping().getPrice();
                                ot.setPrice(p);
                            } else {
                                p = ZERO;
                                ot.setPrice(ZERO);
                            }
                        }
                        extras = extras.add(p);
                    }
                }
                it.setExtrasTotal(extras);

                // lineTotal = (unitPrice + extrasTotal) * quantity
                int qty = (it.getQuantity() == null) ? 0 : it.getQuantity();
                BigDecimal line = unit.add(extras).multiply(BigDecimal.valueOf(qty));
                it.setLineTotal(line);

                itemsTotal = itemsTotal.add(line);
            }
        }

        o.setItemsTotal(itemsTotal);

        if (o.getDiscountTotal() == null) o.setDiscountTotal(ZERO);
        if (o.getDeliveryFee()   == null) o.setDeliveryFee(ZERO);
        if (o.getTipAmount()     == null) o.setTipAmount(ZERO);

        // grandTotal = itemsTotal - discountTotal + deliveryFee + tipAmount
        BigDecimal grand = itemsTotal
                .subtract(o.getDiscountTotal())
                .add(o.getDeliveryFee())
                .add(o.getTipAmount());

        o.setGrandTotal(grand);
    }
}
