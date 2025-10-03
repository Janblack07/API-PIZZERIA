package com.example.apipizzeria.Domain.user.entity;

import com.example.apipizzeria.common.BaseEntity;
import com.example.apipizzeria.common.enums.StaffRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "staff_invites",
        indexes = {@Index(name="ix_staff_invites_email", columnList="email", unique=false),
                @Index(name="ix_staff_invites_token", columnList="token", unique=true)})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffInvite extends BaseEntity {

    @Email
    @NotBlank
    @Column(nullable = false, length = 180)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "staff_invite_roles", joinColumns = @JoinColumn(name = "invite_id"))
    @Column(name = "role", length = 20)
    @Enumerated(EnumType.STRING)
    private Set<StaffRole> roles = new HashSet<>();

    @Column(nullable = false, length = 60, unique = true)
    private String token; // UUID aleatorio para el link de invitación

    private Instant expiresAt; // ej. +7 días
    private boolean accepted;

    @Column(length = 64)
    private String acceptedByFirebaseUid; // quien aceptó (cuando se vincula)
}
