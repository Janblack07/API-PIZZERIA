package com.example.apipizzeria.common.api.staff.dto;

import com.example.apipizzeria.common.enums.StaffRole;

import java.time.Instant;
import java.util.Set;

public record StaffResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        boolean active,
        Set<StaffRole> roles,
        Instant createdAt,
        Instant updatedAt
) {}