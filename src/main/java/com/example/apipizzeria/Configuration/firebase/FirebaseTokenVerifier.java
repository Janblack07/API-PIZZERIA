package com.example.apipizzeria.Configuration.firebase;


public interface FirebaseTokenVerifier {
    FirebaseUserClaims verify(String idToken);
}