package com.example.apipizzeria.common.exception;

import java.time.Instant;
import java.util.Map;

public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String code,      // tu código de negocio (opcional)
        String message,
        String path,
        Object details    // puede ser Map/lista con errores de validación
) {
    public static ApiError of(int status, String error, String code, String message, String path, Object details) {
        return new ApiError(Instant.now(), status, error, code, message, path, details);
    }
}