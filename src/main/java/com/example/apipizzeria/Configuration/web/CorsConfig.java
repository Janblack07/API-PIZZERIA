package com.example.apipizzeria.Configuration.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${cors.allowed-origins:*}")
    private String[] allowedOrigins;

    @Value("${cors.allowed-methods:GET,POST,PUT,PATCH,DELETE,OPTIONS}")
    private String[] allowedMethods;

    @Value("${cors.allowed-headers:*}")
    private String[] allowedHeaders;

    @Value("${cors.exposed-headers:}")
    private String[] exposedHeaders;

    @Value("${cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Value("${cors.max-age:3600}")
    private long maxAge;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();

        // Nota: si allowCredentials=true NO puedes usar "*"
        List<String> origins = Arrays.asList(allowedOrigins);
        if (allowCredentials && origins.contains("*")) {
            // evita confusión en dev; en credenciales debe ser lista explícita
            origins = List.of("http://localhost:4200","http://localhost:8100");
        }
        cfg.setAllowedOrigins(origins);
        cfg.setAllowedMethods(Arrays.asList(allowedMethods));
        cfg.setAllowedHeaders(Arrays.asList(allowedHeaders));

        if (exposedHeaders != null && exposedHeaders.length > 0 && !exposedHeaders[0].isBlank()) {
            cfg.setExposedHeaders(Arrays.asList(exposedHeaders));
        }

        cfg.setAllowCredentials(allowCredentials);
        cfg.setMaxAge(maxAge);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica a todos los endpoints
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}