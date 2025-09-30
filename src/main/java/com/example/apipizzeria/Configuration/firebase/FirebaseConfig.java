package com.example.apipizzeria.Configuration.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;


import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    private final FirebaseProperties props;
    private final ResourceLoader loader;

    public FirebaseConfig(FirebaseProperties props, ResourceLoader loader) {
        this.props = props;
        this.loader = loader;
    }

    @PostConstruct
    public void init() throws Exception {
        if (!FirebaseApp.getApps().isEmpty()) return;

        FirebaseOptions.Builder builder = FirebaseOptions.builder();

        String path = props.getCredentialsPath();
        if (path != null && !path.isBlank()) {
            Resource res = loader.getResource(path);   // soporta classpath:, file:
            if (!res.exists()) {
                throw new IllegalStateException("Firebase credentials not found at: " + path);
            }
            try (InputStream in = res.getInputStream()) {
                builder.setCredentials(GoogleCredentials.fromStream(in));
            }
        } else {
            // Fallback a GOOGLE_APPLICATION_CREDENTIALS o ADC
            builder.setCredentials(GoogleCredentials.getApplicationDefault());
        }

        FirebaseApp.initializeApp(builder.build());
    }
}