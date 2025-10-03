package com.example.apipizzeria.common.api.staff.dto;

import com.example.apipizzeria.common.enums.StaffRole;
import jakarta.validation.constraints.*;
import java.util.Set;

public record StaffCreateRequest(
        @NotBlank @Size(max = 160) String fullName,
        @NotBlank @Email @Size(max = 180) String email,
        @Size(max = 100) String phone,
        @NotEmpty Set<StaffRole> roles
) {}