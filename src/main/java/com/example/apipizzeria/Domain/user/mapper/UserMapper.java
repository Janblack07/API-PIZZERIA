package com.example.apipizzeria.Domain.user.mapper;

import com.example.apipizzeria.Domain.user.dto.UserDTO;
import com.example.apipizzeria.Domain.user.entity.User;
public final class UserMapper {
    private UserMapper() {}
    public static UserDTO toDTO(User u) {
        return new UserDTO(
                u.getId(),
                u.getFullName(),
                u.getEmail(),
                u.getPhone(),
                u.getKind(),
                u.getStaffRoles(),
                u.isActive()
        );
    }
}
