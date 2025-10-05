package com.example.apipizzeria.common.api.address.controller;

import com.example.apipizzeria.common.api.ApiPageUtil;
import com.example.apipizzeria.common.api.ApiResponse;
import com.example.apipizzeria.common.api.address.dto.*;
import com.example.apipizzeria.common.api.address.service.CustomerAddressService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me/addresses")
@PreAuthorize("isAuthenticated()") // ya filtras por owner en el servicio
public class CustomerAddressController {

    private final CustomerAddressService service;

    public CustomerAddressController(CustomerAddressService service) {
        this.service = service;
    }

    @Operation(summary = "Listar mis direcciones (opcional: solo favoritas)")
    @GetMapping
    public ResponseEntity<ApiResponse<?>> list(
            @RequestParam(required = false) Boolean favorite,
            Pageable pageable
    ) {
        Page<AddressResponse> page = service.listMine(favorite, pageable);
        var meta = ApiPageUtil.meta(page);
        var body = ApiResponse.withMeta(200, page.getContent(), meta, null);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Obtener una dirección mía")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> get(@PathVariable Long id) {
        var dto = service.getMine(id);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @Operation(summary = "Crear dirección")
    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> create(@Valid @RequestBody AddressCreateRequest request) {
        var dto = service.createMine(request);
        return ResponseEntity.status(201).body(ApiResponse.created(dto));
    }

    @Operation(summary = "Actualizar dirección")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody AddressUpdateRequest request
    ) {
        var dto = service.updateMine(id, request);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @Operation(summary = "Marcar como favorita")
    @PatchMapping("/{id}/favorite")
    public ResponseEntity<ApiResponse<AddressResponse>> setFavorite(@PathVariable Long id) {
        var dto = service.setFavoriteMine(id);
        return ResponseEntity.ok(ApiResponse.ok(dto, "Dirección marcada como favorita"));
    }

    @Operation(summary = "Eliminar dirección")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.deleteMine(id);
        return ResponseEntity.ok(ApiResponse.noContent("Dirección eliminada"));
    }
}