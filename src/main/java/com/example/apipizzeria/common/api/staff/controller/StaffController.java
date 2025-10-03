package com.example.apipizzeria.common.api.staff.controller;

import com.example.apipizzeria.common.api.ApiPage;
import com.example.apipizzeria.common.api.ApiResponse;
import com.example.apipizzeria.common.api.staff.dto.*;
import com.example.apipizzeria.common.api.staff.service.StaffService;
import com.example.apipizzeria.common.enums.StaffRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.apipizzeria.common.api.ApiPageUtil;

import java.util.List;

@RestController
@RequestMapping("/admin/staff")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAuthority('TYPE_ADMIN')")
public class StaffController {

    private final StaffService service;

    public StaffController(StaffService service) {
        this.service = service;
    }

    @Operation(summary = "Crear un miembro del staff y asignar roles")
    @PostMapping
    public ResponseEntity<ApiResponse<StaffResponse>> create(@Valid @RequestBody StaffCreateRequest body) {
        var dto = service.create(body);
        return ResponseEntity.status(201).body(ApiResponse.created(dto));
    }

    @Operation(summary = "Listar staff")
    @GetMapping
    public ResponseEntity<ApiResponse<List<StaffResponse>>> list(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) StaffRole role,
            Pageable pageable
    ) {
        Page<StaffResponse> page = service.list(active, role, pageable);
        var meta = ApiPageUtil.meta(page);                               // meta con paginación
        var body = ApiResponse.withMeta(200, page.getContent(), meta, null); // data = lista
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Obtener staff por id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.get(id)));
    }

    @Operation(summary = "Actualizar datos y/o roles (replace si se envían)")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody StaffUpdateRequest body
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, body)));
    }

    @Operation(summary = "Reemplazar roles del staff")
    @PutMapping("/{id}/roles")
    public ResponseEntity<ApiResponse<StaffResponse>> replaceRoles(
            @PathVariable Long id,
            @Valid @RequestBody StaffRolesRequest body
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.replaceRoles(id, body.roles())));
    }

    @Operation(summary = "Activar/Desactivar staff")
    @PatchMapping("/{id}/active")
    public ResponseEntity<ApiResponse<StaffResponse>> setActive(
            @PathVariable Long id,
            @RequestParam boolean value
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.setActive(id, value)));
    }

    @Operation(summary = "Eliminar (soft delete -> Desactivar)")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.noContent("Eliminado (inactivado)"));
    }
}
