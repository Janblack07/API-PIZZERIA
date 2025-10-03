package com.example.apipizzeria.Configuration.firebase;

import java.time.Instant;

public record FirebaseUserInfo(
        String email,
        String uid,
        boolean emailVerified,
        String name,
        String picture,
        Instant authTime) {}