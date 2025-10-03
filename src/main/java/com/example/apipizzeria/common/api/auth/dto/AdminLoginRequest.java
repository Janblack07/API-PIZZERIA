package com.example.apipizzeria.common.api.auth.dto;

public record AdminLoginRequest(
        String email,
        String password) {
}
