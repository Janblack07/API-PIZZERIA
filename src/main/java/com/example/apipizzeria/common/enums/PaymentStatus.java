package com.example.apipizzeria.common.enums;

/** Estado de la transacción de pago */
public enum PaymentStatus {
    PENDING, //pendiente
    PAID, //pagada
    FAILED, //fallo
    REFUNDED //reintegrado
}