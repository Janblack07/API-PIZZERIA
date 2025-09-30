package com.example.apipizzeria.Configuration.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI api() {
        // Security scheme para Bearer JWT
        final String bearerKey = "bearerAuth";
        SecurityScheme bearer = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        return new OpenAPI()
                .info(new Info()
                        .title("Pizzería API")
                        .version("v1")
                        .description("Backend Pizzería (Spring Boot + Firebase + JWT + Cloudinary)")
                        .contact(new Contact().name("Equipo API-PIZZA"))
                        .license(new License().name("Proprietary")))
                .addSecurityItem(new SecurityRequirement().addList(bearerKey))
                .components(new io.swagger.v3.oas.models.Components().addSecuritySchemes(bearerKey, bearer));
    }
}