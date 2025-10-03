package com.example.apipizzeria.common.api.auth.dto;

import com.example.apipizzeria.Domain.user.dto.UserDTO;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserDTO user
) {}
