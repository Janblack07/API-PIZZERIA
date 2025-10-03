package com.example.apipizzeria.common.api.staff.dto;

import com.example.apipizzeria.common.enums.StaffRole;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record StaffUpdateRequest(
        @Size(max = 160) String fullName,
        @Size(max = 100) String phone,
        Set<StaffRole> roles
) {}