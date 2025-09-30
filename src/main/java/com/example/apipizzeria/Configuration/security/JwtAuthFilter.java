package com.example.apipizzeria.Configuration.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final AntPathMatcher matcher = new AntPathMatcher();

    // Rutas públicas: NO deben pasar por el filtro
    private static final List<String> PUBLIC = List.of(
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/webjars/**",
            "/favicon.ico",
            "/error"
    );

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /** Evita filtrar las rutas públicas (Swagger, auth, etc.) */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return PUBLIC.stream().anyMatch(p -> matcher.match(p, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // Sin bearer -> no autenticamos; dejamos que Security decida (permitAll / authenticated)
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        try {
            // Debe devolver los claims que generas en tu JwtService
            Map<String, Object> claims = jwtService.parseAndValidate(token);

            // Claims esperados: uid (Long), email (String), user_type (String), roles (List<String>)
            Long userId = claims.get("uid") != null ? ((Number) claims.get("uid")).longValue() : null;
            String email = (String) claims.get("email");
            String userType = (String) claims.get("user_type");

            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.getOrDefault("roles", List.of());

            // Construir authorities: "TYPE_<USER_TYPE>" + roles simples (COOK, CASHIER, DRIVER, …)
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (userType != null && !userType.isBlank()) {
                authorities.add(new SimpleGrantedAuthority("TYPE_" + userType));
            }
            authorities.addAll(roles.stream()
                    .filter(Objects::nonNull)
                    .filter(s -> !s.isBlank())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList()));

            // Principal = email (puedes usar un UserDetails propio si quieres)
            Authentication auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception ex) {
            // Token inválido/expirado: limpiamos contexto y dejamos que el EntryPoint responda 401
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }
}
