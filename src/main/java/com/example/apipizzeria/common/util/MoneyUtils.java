package com.example.apipizzeria.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

public final class MoneyUtils {
    private static final int SCALE = 2;

    private MoneyUtils(){}

    public static BigDecimal zero() {
        return BigDecimal.ZERO.setScale(SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal of(double value) {
        return BigDecimal.valueOf(value).setScale(SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return scale(a).add(scale(b)).setScale(SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal multiply(BigDecimal price, int qty) {
        return scale(price).multiply(BigDecimal.valueOf(qty)).setScale(SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal percent(BigDecimal base, double pct) {
        return scale(base)
                .multiply(BigDecimal.valueOf(pct))
                .divide(BigDecimal.valueOf(100), SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal applyPercentDiscount(BigDecimal base, double pct) {
        return scale(base).subtract(percent(base, pct)).max(zero());
    }

    public static BigDecimal applyFixedDiscount(BigDecimal base, BigDecimal amount) {
        return scale(base).subtract(scale(amount)).max(zero());
    }

    public static String format(BigDecimal amount, Locale locale) {
        return java.text.NumberFormat.getCurrencyInstance(locale).format(scale(amount));
    }

    private static BigDecimal scale(BigDecimal v) {
        return (v == null ? zero() : v.setScale(SCALE, RoundingMode.HALF_UP));
    }
}