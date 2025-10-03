package com.example.apipizzeria.Domain.user.entity;

import com.example.apipizzeria.common.BaseEntity;
import com.example.apipizzeria.common.enums.StaffRole;
import com.example.apipizzeria.common.enums.UserKind;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "ix_users_email", columnList = "email", unique = true),
                @Index(name = "ix_users_kind", columnList = "kind"),
                @Index(name = "ix_users_active", columnList = "active")
        }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User extends BaseEntity {

    @NotBlank
    @Column(nullable = false, length = 160)
    private String fullName;

    @Email
    @NotBlank
    @Column(nullable = false, length = 180, unique = true)
    private String email;

    @Column(length = 100)
    private String phone;

    @JsonIgnore
    @Column(length = 120)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserKind kind;

    /** STAFF roles múltiples (COOK/CASHIER/DRIVER). Mejor LAZY por rendimiento. */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_staff_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", length = 20)
    @Enumerated(EnumType.STRING)
    private Set<StaffRole> staffRoles = new HashSet<>();

    /** Push notifications */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_device_tokens", joinColumns = @JoinColumn(name="user_id"))
    @Column(name = "device_token", length = 255)
    private Set<String> deviceTokens = new HashSet<>();

    /** Direcciones del cliente */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Address> addresses = new HashSet<>();

    /** Firebase link */
    @Column(length = 64, unique = true)
    private String firebaseUid;

    private Instant emailVerifiedAt; // útil si quieres dejar constancia
    private Instant lastLoginAt;

    private boolean active = true;
}
