package com.example.apipizzeria.common.api.auth.service.impl;


import com.example.apipizzeria.Configuration.firebase.FirebaseAuthService;
import com.example.apipizzeria.Configuration.firebase.FirebaseUserInfo;
import com.example.apipizzeria.Domain.user.dto.UserDTO;
import com.example.apipizzeria.Domain.user.entity.User;
import com.example.apipizzeria.Domain.user.mapper.UserMapper;
import com.example.apipizzeria.Domain.user.repository.UserRepository;

import com.example.apipizzeria.common.api.auth.dto.AuthResponse;
import com.example.apipizzeria.common.api.auth.service.AuthService;
import com.example.apipizzeria.common.enums.UserKind;
import com.example.apipizzeria.common.exception.ApiException;

import com.example.apipizzeria.Configuration.security.JwtService;
import com.example.apipizzeria.common.exception.ErrorCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AuthServiceImpl implements AuthService {

    private final FirebaseAuthService firebaseAuthService;
    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthServiceImpl(
            FirebaseAuthService firebaseAuthService,
            UserRepository userRepo,
            JwtService jwtService,
            AuthenticationManager authManager
    ) {
        this.firebaseAuthService = firebaseAuthService;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    @Override
    @Transactional
    public AuthResponse loginWithFirebase(String idToken) {
        // 1) Verificar token con Firebase
        FirebaseUserInfo info = firebaseAuthService.verifyIdToken(idToken);

        // 2) Buscar usuario por email
        User user = userRepo.findByEmail(info.email())
                .orElseGet(() -> {
                    // Si no existe: crear CLIENT por defecto
                    User u = User.builder()
                            .email(info.email())
                            .fullName(info.name() != null ? info.name() : info.email())
                            .kind(UserKind.CUSTOMER)
                            .active(true)
                            .build();
                    return userRepo.save(u);
                });

        // (opcional) actualizar metadata
        user.setFullName(user.getFullName() == null ? info.name() : user.getFullName());
        // user.setLastLoginAt(Instant.now()); // si tienes este campo en BaseEntity extiende

        // 3) Emitir tokens
        String access = jwtService.issueAccessToken(user);
        String refresh = jwtService.issueRefreshToken(user);

        return new AuthResponse(access, refresh, UserMapper.toDTO(user));
    }

    @Override
    public AuthResponse loginAdmin(String email, String password) {
        // 1) Autenticar con Spring Security (UserDetailsService si lo tienes) o manual
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (Exception ex) {
            throw ApiException.unauthorized("Credenciales inválidas", ErrorCode.UNAUTHORIZED);
        }

        // 2) Debe existir y ser ADMIN
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> ApiException.unauthorized("Usuario no encontrado", ErrorCode.UNAUTHORIZED));

        if (user.getKind() != UserKind.ADMIN) {
            throw ApiException.forbidden("No es administrador", ErrorCode.FORBIDDEN);
        }
        if (!user.isActive()) {
            throw ApiException.forbidden("Usuario inactivo", ErrorCode.FORBIDDEN);
        }

        String access = jwtService.issueAccessToken(user);
        String refresh = jwtService.issueRefreshToken(user);
        return new AuthResponse(access, refresh, UserMapper.toDTO(user));
    }

    @Override
    public AuthResponse refresh(String refreshToken) {
        var claims = jwtService.parseAndValidate(refreshToken);
        Object emailObj = claims.get("email");
        if (emailObj == null) throw ApiException.unauthorized("Refresh inválido", ErrorCode.UNAUTHORIZED);

        User user = userRepo.findByEmail(String.valueOf(emailObj))
                .orElseThrow(() -> ApiException.unauthorized("Usuario no existe", ErrorCode.UNAUTHORIZED));

        String newAccess = jwtService.issueAccessToken(user);
        String newRefresh = jwtService.issueRefreshToken(user);
        return new AuthResponse(newAccess, newRefresh, UserMapper.toDTO(user));
    }

    @Override
    public UserDTO me() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw ApiException.unauthorized("No autenticado", ErrorCode.UNAUTHORIZED);
        }
        String email = auth.getName();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> ApiException.unauthorized("Usuario no existe", ErrorCode.UNAUTHORIZED));
        return UserMapper.toDTO(user);
    }
}
