package com.example.apipizzeria.common.api.auth.service;


import com.example.apipizzeria.Domain.user.dto.UserDTO;
import com.example.apipizzeria.common.api.auth.dto.AuthResponse;

public interface AuthService {
    AuthResponse loginWithFirebase(String idToken);
    AuthResponse loginAdmin(String email, String password);
    AuthResponse refresh(String refreshToken);
    UserDTO me(); // opcional, devolver el usuario actual
}