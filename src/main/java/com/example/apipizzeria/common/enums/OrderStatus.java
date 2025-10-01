package com.example.apipizzeria.common.enums;

public enum OrderStatus {
    NEW,         // creado
    PREPARING,   // preparando (mesa de armado)
    BAKING,      // en horno
    READY,       // listo para retirar/entregar
    ON_ROUTE,    // en ruta con repartidor
    DELIVERED,   // entregado
    CANCELLED    // cancelado
}