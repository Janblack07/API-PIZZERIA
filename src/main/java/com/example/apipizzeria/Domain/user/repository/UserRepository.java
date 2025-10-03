package com.example.apipizzeria.Domain.user.repository;


import com.example.apipizzeria.Domain.user.entity.User;
import com.example.apipizzeria.common.enums.StaffRole;
import com.example.apipizzeria.common.enums.UserKind;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByKind(UserKind kind);
    boolean existsByEmailIgnoreCase(String email);
    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByIdAndKind(Long id, UserKind kind);
    Page<User> findByKind(UserKind kind, Pageable pageable);
    Page<User> findByKindAndActive(UserKind kind, boolean active, Pageable pageable);

    // Para filtrar por rol
    Page<User> findByKindAndStaffRolesContaining(UserKind kind, StaffRole role, Pageable pageable);
    Page<User> findByKindAndActiveAndStaffRolesContaining(UserKind kind, boolean active, StaffRole role, Pageable pageable);
}