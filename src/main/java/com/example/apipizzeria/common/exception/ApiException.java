package com.example.apipizzeria.common.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final String code; // opcional: código propio (e.g. USER_NOT_FOUND)

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.code = null;
    }
    public ApiException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public HttpStatus getStatus() { return status; }
    public String getCode() { return code; }

    /* ===== Overloads (2 args) para tu servicio ===== */
    public static ApiException unauthorized(String message, ErrorCode errorCode) {
        return new ApiException(HttpStatus.UNAUTHORIZED, errorCode.name(), message);
    }
    public static ApiException forbidden(String message, ErrorCode errorCode) {
        return new ApiException(HttpStatus.FORBIDDEN, errorCode.name(), message);
    }
}