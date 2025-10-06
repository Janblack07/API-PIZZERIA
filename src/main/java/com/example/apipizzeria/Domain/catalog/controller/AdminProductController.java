package com.example.apipizzeria.Domain.catalog.controller;

import com.example.apipizzeria.Domain.catalog.dto.ProductDetailDTO;
import com.example.apipizzeria.Domain.catalog.dto.ProductListItemDTO;
import com.example.apipizzeria.Domain.catalog.dto.ProductUpsertRequest;
import com.example.apipizzeria.Domain.catalog.service.AdminProductService;
import com.example.apipizzeria.common.api.*;
import com.example.apipizzeria.common.enums.ProductType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Productos", description = "Gestion de los productos")
@RestController
@RequestMapping("/admin/products")
@PreAuthorize("hasAuthority('TYPE_ADMIN')")
public class AdminProductController {

    private final AdminProductService service;

    public AdminProductController(AdminProductService service) { this.service = service; }

    @Operation(
            summary = "Crear producto",
            description = "Crea un producto (PIZZA, BEVERAGE, DESSERT o COMBO). "
                    + "Permite imagen principal, galería, variantes (size/dough/price), "
                    + "toppings permitidos (si es pizza/customizable) y combo items (si es COMBO)."
    )
    @PostMapping
    public ApiResponse<ProductDetailDTO> create(@Valid @RequestBody ProductUpsertRequest req) {
        return ApiResponse.created(service.create(req));
    }
    @Operation(
            summary = "Actualizar producto",
            description = "Actualiza todos los datos del producto. "
                    + "Las colecciones (variantes, toppings permitidos, combo items) se reemplazan completamente."
    )

    @PutMapping("/{id}")
    public ApiResponse<ProductDetailDTO> update(@PathVariable Long id, @Valid @RequestBody ProductUpsertRequest req) {
        return ApiResponse.ok(service.update(id, req), "Producto actualizado");
    }
    @Operation(
            summary = "Activar/Desactivar producto",
            description = "Cambia el estado activo del producto (soft delete cuando se Desactiva)."
    )
    @PatchMapping("/{id}/active")
    public ApiResponse<ProductDetailDTO> setActive(@PathVariable Long id, @RequestParam boolean active) {
        return ApiResponse.ok(service.setActive(id, active), "Estado actualizado");
    }
    @Operation(
            summary = "Eliminar producto (soft delete)",
            description = "Inactiva el producto. No elimina físicamente los registros."
    )
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ApiResponse.noContent("Producto Desactivado.");
    }
    @Operation(
            summary = "Obtener detalle de producto",
            description = "Devuelve detalle completo del producto (incluye variantes, imágenes y asociaciones)."
    )

    @GetMapping("/{id}")
    public ApiResponse<ProductDetailDTO> get(@PathVariable Long id) {
        return ApiResponse.ok(service.get(id));
    }
    @Operation(
            summary = "Listar productos (admin)",
            description = "Listado paginado con filtros opcionales: type, q (búsqueda por nombre) y active (true/false/null)."
    )
    @GetMapping
    public ApiResponse<ApiPage<ProductListItemDTO>> list(
            @RequestParam(required = false) ProductType type,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Boolean active,
            Pageable pageable
    ) {
        Page<ProductListItemDTO> page = service.list(type, q, active, pageable);
        return ApiResponse.ok(ApiPageUtil.meta(page));
    }
}
