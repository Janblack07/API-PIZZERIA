package com.example.apipizzeria.common.api.staff.dto;

import com.example.apipizzeria.common.enums.StaffRole;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record StaffRolesRequest(
        @NotEmpty Set<StaffRole> roles
) {}
