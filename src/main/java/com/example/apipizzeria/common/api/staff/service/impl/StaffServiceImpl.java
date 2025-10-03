package com.example.apipizzeria.common.api.staff.service.impl;

import com.example.apipizzeria.Domain.user.entity.User;
import com.example.apipizzeria.Domain.user.repository.UserRepository;
import com.example.apipizzeria.common.api.staff.dto.*;
import com.example.apipizzeria.common.api.staff.mapper.StaffMapper;
import com.example.apipizzeria.common.api.staff.service.StaffService;
import com.example.apipizzeria.common.enums.StaffRole;
import com.example.apipizzeria.common.enums.UserKind;
import com.example.apipizzeria.common.exception.BadRequestException;
import com.example.apipizzeria.common.exception.ConflictException;
import com.example.apipizzeria.common.exception.NotFoundException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {

    private final UserRepository userRepo;

    public StaffServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public StaffResponse create(StaffCreateRequest request) {
        String email = request.email().trim().toLowerCase();

        // 1) email único
        if (userRepo.existsByEmail(email)) {
            throw new ConflictException("El correo ya está registrado");
        }

        // 2) construir Staff
        User u = StaffMapper.fromCreate(request);
        u.getStaffRoles().addAll(request.roles());
        u.setKind(UserKind.STAFF);

        // 3) guardar
        u = userRepo.save(u);

        // 4) (opcional) enviar invitación por email con link de onboarding

        return StaffMapper.toDTO(u);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StaffResponse> list(Boolean active, StaffRole role, Pageable pageable) {
        Page<User> page;

        if (active == null && role == null) {
            page = userRepo.findByKind(UserKind.STAFF, pageable);
        } else if (active != null && role == null) {
            page = userRepo.findByKindAndActive(UserKind.STAFF, active, pageable);
        } else if (active == null) {
            page = userRepo.findByKindAndStaffRolesContaining(UserKind.STAFF, role, pageable);
        } else {
            page = userRepo.findByKindAndActiveAndStaffRolesContaining(
                    UserKind.STAFF, active, role, pageable
            );
        }

        return page.map(StaffMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public StaffResponse get(Long id) {
        User u = userRepo.findByIdAndKind(id, UserKind.STAFF)
                .orElseThrow(() ->  new NotFoundException("Staff no encontrado"));
        return StaffMapper.toDTO(u);
    }

    @Override
    public StaffResponse update(Long id, StaffUpdateRequest request) {
        User u = userRepo.findByIdAndKind(id, UserKind.STAFF)
                .orElseThrow(() ->  new NotFoundException("Staff no encontrado"));

        StaffMapper.applyUpdate(u, request);
        return StaffMapper.toDTO(u);
    }

    @Override
    public StaffResponse setActive(Long id, boolean active) {
        User u = userRepo.findByIdAndKind(id, UserKind.STAFF)
                .orElseThrow(() ->  new NotFoundException("Staff no encontrado"));
        u.setActive(active);
        return StaffMapper.toDTO(u);
    }

    @Override
    public StaffResponse replaceRoles(Long id, Set<StaffRole> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new BadRequestException("Debe enviar al menos un rol");
        }
        User u = userRepo.findByIdAndKind(id, UserKind.STAFF)
                .orElseThrow(() -> new BadRequestException("Staff no encontrado"));
        u.getStaffRoles().clear();
        u.getStaffRoles().addAll(roles);
        return StaffMapper.toDTO(u);
    }

    @Override
    public void delete(Long id) {
        // política: soft-delete -> inactivar
        User u = userRepo.findByIdAndKind(id, UserKind.STAFF)
                .orElseThrow(() -> new NotFoundException("Staff no encontrado"));
        u.setActive(false);
    }
}