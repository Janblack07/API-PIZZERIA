package com.example.apipizzeria.Configuration.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

@Component
public class FirebaseTokenVerifierImpl implements FirebaseTokenVerifier {

    private final FirebaseProperties props;

    public FirebaseTokenVerifierImpl(FirebaseProperties props) { this.props = props; }

    @Override
    public FirebaseUserClaims verify(String idToken) {
        final FirebaseToken t;
        try {
            t = FirebaseAuth.getInstance().verifyIdToken(idToken, true); // true = respeta revocaciones
        } catch (FirebaseAuthException ex) {
            throw new InvalidFirebaseTokenException("INVALID_FIREBASE_TOKEN: " + ex.getAuthErrorCode(), ex);
        }

        String provider = extractProvider(t);
        if (props.getAllowedProviders() != null && !props.getAllowedProviders().isEmpty()) {
            if (provider == null || !props.getAllowedProviders().contains(provider)) {
                throw new InvalidFirebaseTokenException("PROVIDER_NOT_ALLOWED: " + provider);
            }
        }
        if (props.isRequireEmailVerified() && (t.getEmail() == null || !t.isEmailVerified())) {
            throw new InvalidFirebaseTokenException("EMAIL_NOT_VERIFIED");
        }

        return new FirebaseUserClaims(
                t.getUid(),
                t.getEmail(),
                t.isEmailVerified(),
                t.getName() != null ? t.getName() : (String) t.getClaims().get("name"),
                t.getPicture(),
                provider,
                extractAuthTime(t)
        );
    }

    private static String extractProvider(FirebaseToken t) {
        Object firebaseObj = t.getClaims().get("firebase");
        if (firebaseObj instanceof Map<?, ?> map) {
            Object prov = map.get("sign_in_provider");
            return prov == null ? null : String.valueOf(prov);
        }
        return null;
    }

    private static Instant extractAuthTime(FirebaseToken t) {
        Object at = t.getClaims().get("auth_time");
        if (at instanceof Number n) return Instant.ofEpochSecond(n.longValue());
        return Instant.now();
    }
}
