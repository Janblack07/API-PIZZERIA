package com.example.apipizzeria.common.api.auth.controller;


import com.example.apipizzeria.Domain.user.dto.UserDTO;
import com.example.apipizzeria.common.api.auth.dto.AdminLoginRequest;
import com.example.apipizzeria.common.api.auth.dto.AuthResponse;

import com.example.apipizzeria.common.api.ApiResponse;

import com.example.apipizzeria.common.api.auth.dto.LoginFirebaseRequest;
import com.example.apipizzeria.common.api.auth.dto.RefreshTokenRequest;
import com.example.apipizzeria.common.api.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "auth", description = "Autenticación (Firebase/ADMIN) y tokens")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login con Firebase ID Token (Google/Apple/etc)")
    @PostMapping("/login/firebase")
    public ResponseEntity<ApiResponse<AuthResponse>> loginWithFirebase(@RequestBody @Valid LoginFirebaseRequest req) {
        AuthResponse result = authService.loginWithFirebase(req.idToken());
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @Operation(summary = "Login de administrador (email/password local)")
    @PostMapping("/login/admin")
    public ResponseEntity<ApiResponse<AuthResponse>> loginAdmin(@RequestBody @Valid AdminLoginRequest req) {
        AuthResponse result = authService.loginAdmin(req.email(), req.password());
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @Operation(summary = "Refrescar AccessToken usando RefreshToken")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody @Valid RefreshTokenRequest req) {
        AuthResponse result = authService.refresh(req.refreshToken());
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @Operation(summary = "Usuario actual (requiere Bearer)")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> me() {
        UserDTO me = authService.me();
        return ResponseEntity.ok(ApiResponse.ok(me));
    }
}