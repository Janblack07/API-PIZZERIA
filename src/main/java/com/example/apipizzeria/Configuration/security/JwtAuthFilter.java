package com.example.apipizzeria.Configuration.security;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        try {
            Map<String, Object> claims = jwtService.parseAndValidate(token);

            Long userId = ((Number) claims.get("uid")).longValue();
            String email = (String) claims.get("email");
            String userType = (String) claims.get("user_type");

            // roles: ["COOK","CASHIER",...]
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.getOrDefault("roles", List.of());

            // Authorities: TYPE_ADMIN + roles simples
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (userType != null) {
                authorities.add(new SimpleGrantedAuthority("TYPE_" + userType));
            }
            authorities.addAll(
                    roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
            );

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    email, null, authorities // principal = email (puedes crear un UserDetails propio si quieres)
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception ex) {
            // Token inválido: limpiamos contexto y seguimos hacia el entry point
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }
}