package com.example.apipizzeria.Configuration.security;

import com.example.apipizzeria.Domain.user.entity.User;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public interface JwtService {
    String issueAccessToken(User user);
    String issueRefreshToken(User user);
    Map<String, Object> parseAndValidate(String token);
}