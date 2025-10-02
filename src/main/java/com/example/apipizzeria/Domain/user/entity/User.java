package com.example.apipizzeria.Domain.user.entity;


import com.example.apipizzeria.common.BaseEntity;
import com.example.apipizzeria.common.enums.StaffRole;
import com.example.apipizzeria.common.enums.UserKind;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "users",
        indexes = {@Index(name="ix_users_email", columnList = "email", unique = true)})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User extends BaseEntity {

    @Column(nullable = false, length = 160)
    private String fullName;

    @Column(nullable = false, length = 180, unique = true)
    private String email;

    @Column(length = 100)
    private String phone;

    // Password nullable si login social
    @Column(length = 120)
    private String passwordHash;

    // CLIENT, ADMIN, STAFF
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserKind kind;

    // Para STAFF: múltiples roles (COOK/CASHIER/DRIVER)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_staff_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", length = 20)
    @Enumerated(EnumType.STRING)
    private Set<StaffRole> staffRoles = new HashSet<>();

    // Para push notifications (opcional; N tokens)
    @ElementCollection
    @CollectionTable(name = "user_device_tokens", joinColumns = @JoinColumn(name="user_id"))
    @Column(name = "device_token", length = 255)
    private Set<String> deviceTokens = new HashSet<>();

    // Direcciones del cliente
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Address> addresses = new HashSet<>();

    private boolean active = true;
}