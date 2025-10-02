package com.example.apipizzeria.Domain.user.repository;


import com.example.apipizzeria.Domain.user.entity.User;
import com.example.apipizzeria.common.enums.UserKind;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByKind(UserKind kind);
}