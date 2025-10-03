package com.example.apipizzeria.Configuration.firebase;

import org.springframework.stereotype.Service;

@Service
public class FirebaseAuthServiceImpl implements FirebaseAuthService {

    private final FirebaseTokenVerifier verifier;

    public FirebaseAuthServiceImpl(FirebaseTokenVerifier verifier) {
        this.verifier = verifier;
    }

    @Override
    public FirebaseUserInfo verifyIdToken(String idToken) {
        FirebaseUserClaims c = verifier.verify(idToken);
        return new FirebaseUserInfo(
                c.uid(),
                c.email(),
                c.emailVerified(),
                c.name(),
                c.picture(),
                c.authTime()
        );
    }
}
