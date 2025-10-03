package com.example.apipizzeria.Domain.user.dto;

import com.example.apipizzeria.common.enums.UserKind;
import com.example.apipizzeria.common.enums.StaffRole;
import java.util.Set;

public record UserDTO(
        Long id,
        String fullName,
        String email,
        String phone,
        UserKind kind,
        Set<StaffRole> staffRoles,
        boolean active
) {}