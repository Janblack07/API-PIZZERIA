package com.example.apipizzeria.common.api.staff.mapper;

import com.example.apipizzeria.Domain.user.entity.User;
import com.example.apipizzeria.common.api.staff.dto.*;
import com.example.apipizzeria.common.enums.UserKind;

public class StaffMapper {

    public static User fromCreate(StaffCreateRequest r) {
        return User.builder()
                .fullName(r.fullName())
                .email(r.email().trim().toLowerCase())
                .phone(r.phone())
                .kind(UserKind.STAFF)
                .active(true)
                .build();
    }

    public static StaffResponse toDTO(User u) {
        return new StaffResponse(
                u.getId(),
                u.getFullName(),
                u.getEmail(),
                u.getPhone(),
                u.isActive(),
                u.getStaffRoles(),           // Set<StaffRole>
                u.getCreatedAt(),
                u.getUpdatedAt()
        );
    }

    public static void applyUpdate(User u, StaffUpdateRequest r) {
        if (r.fullName() != null && !r.fullName().isBlank()) u.setFullName(r.fullName());
        if (r.phone() != null) u.setPhone(r.phone());
        if (r.roles() != null && !r.roles().isEmpty()) {
            u.getStaffRoles().clear();
            u.getStaffRoles().addAll(r.roles());
        }
    }
}
