package com.example.apipizzeria.Configuration.firebase;

public interface FirebaseAuthService {
    FirebaseUserInfo verifyIdToken(String idToken);
}