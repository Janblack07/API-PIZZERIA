package com.example.apipizzeria.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) { super(HttpStatus.UNAUTHORIZED, message); }
    public UnauthorizedException(String code, String message) { super(HttpStatus.UNAUTHORIZED, code, message); }
}