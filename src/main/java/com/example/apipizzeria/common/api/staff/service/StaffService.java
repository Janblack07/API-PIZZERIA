package com.example.apipizzeria.common.api.staff.service;

import com.example.apipizzeria.common.api.staff.dto.*;
import com.example.apipizzeria.common.enums.StaffRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface StaffService {

    StaffResponse create(StaffCreateRequest request);

    Page<StaffResponse> list(Boolean active, StaffRole role, Pageable pageable);

    StaffResponse get(Long id);

    StaffResponse update(Long id, StaffUpdateRequest request);

    StaffResponse setActive(Long id, boolean active);

    StaffResponse replaceRoles(Long id, Set<StaffRole> roles);

    void delete(Long id); // opcional (soft delete = inactivar)
}
