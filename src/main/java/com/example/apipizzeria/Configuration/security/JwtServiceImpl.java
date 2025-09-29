package com.example.apipizzeria.Configuration.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
public class JwtServiceImpl implements JwtService {

    private final Key key;

    @Value("${security.jwt.issuer:PizzeriaAPI}")
    private String issuer;

    public JwtServiceImpl(@Value("${security.jwt.secret}") String secret) {
        byte[] bytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    @Override
    public String generateAccessToken(Long userId, String email, String userType, List<String> roles, int tokenVersion, Duration ttl) {
        Instant now = Instant.now(), exp = now.plus(ttl == null ? Duration.ofMinutes(15) : ttl);

        return Jwts.builder()
                .issuer(issuer)
                .subject(String.valueOf(userId))
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claim("uid", userId)
                .claim("email", email)
                .claim("user_type", userType)
                .claim("roles", roles == null ? List.of() : roles)
                .claim("ver", tokenVersion)
                .signWith(key)                 // en 0.12: con key HMAC usa HS256 por defecto
                .compact();
    }

    @Override
    public String generateRefreshToken(Long userId, Duration ttl) {
        Instant now = Instant.now(), exp = now.plus(ttl == null ? Duration.ofDays(14) : ttl);
        return Jwts.builder()
                .issuer(issuer)
                .subject("refresh:" + userId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claim("uid", userId)
                .signWith(key)
                .compact();
    }

    @Override
    public Map<String, Object> parseAndValidate(String token) {
        // Funciona tanto en 0.11.x como 0.12.x
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(key)             // valida firma HS256 con tu key
                .build()
                .parseClaimsJws(token);         // lanza excepción si es inválido/expirado

        Claims c = jws.getBody();
        return new java.util.LinkedHashMap<>(c);
    }

}