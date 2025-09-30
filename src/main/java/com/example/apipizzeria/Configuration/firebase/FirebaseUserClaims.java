package com.example.apipizzeria.Configuration.firebase;

import java.time.Instant;

public record FirebaseUserClaims(
        String uid,
        String email,
        boolean emailVerified,
        String name,
        String picture,
        String provider,   // google.com | microsoft.com
        Instant authTime
) {}