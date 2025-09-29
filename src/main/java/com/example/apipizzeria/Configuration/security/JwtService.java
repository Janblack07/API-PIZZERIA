package com.example.apipizzeria.Configuration.security;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public interface JwtService {
    String generateAccessToken(Long userId, String email, String userType, List<String> roles, int tokenVersion, Duration ttlSeconds);
    String generateRefreshToken(Long userId, Duration ttlSeconds);
    Map<String, Object> parseAndValidate(String token);
}