package com.example.apipizzeria.common.exception;

import org.springframework.http.HttpStatus;

public class ServiceUnavailableException extends ApiException {
    public ServiceUnavailableException(String message) { super(HttpStatus.SERVICE_UNAVAILABLE, message); }
    public ServiceUnavailableException(String code, String message) { super(HttpStatus.SERVICE_UNAVAILABLE, code, message); }
}