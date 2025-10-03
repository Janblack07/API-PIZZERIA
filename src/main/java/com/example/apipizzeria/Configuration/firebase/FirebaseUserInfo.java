package com.example.apipizzeria.Configuration.firebase;

public record FirebaseUserInfo(
        String email,
        String uid,
        boolean emailVerified,
        String name,
        String picture) {}