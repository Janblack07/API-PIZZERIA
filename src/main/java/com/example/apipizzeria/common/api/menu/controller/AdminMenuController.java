package com.example.apipizzeria.common.api.menu.controller;

import com.example.apipizzeria.Domain.catalog.dto.ProductListItemDTO;
import com.example.apipizzeria.common.api.ApiResponse;
import com.example.apipizzeria.common.api.menu.service.AdminMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Menú (Admin)", description = "Activar/Desactivar productos visibles en el catálogo público")
@RestController
@RequestMapping("/admin/menu/products")
public class AdminMenuController {

    private final AdminMenuService adminMenu;

    public AdminMenuController(AdminMenuService adminMenu) {
        this.adminMenu = adminMenu;
    }

    @Operation(summary = "Activar producto en el menú",
            description = "Marca el producto como activo para que aparezca en /menu.")
    @PreAuthorize("hasAuthority('TYPE_ADMIN')")
    @PostMapping("/{id}/activate")
    public ApiResponse<ProductListItemDTO> activate(@PathVariable Long id) {
        return ApiResponse.ok(adminMenu.activateProduct(id), "Producto activado");
    }

    @Operation(summary = "Desactivar producto del menú",
            description = "Oculta el producto del /menu (no lo elimina del módulo Admin).")
    @PreAuthorize("hasAuthority('TYPE_ADMIN')")
    @PostMapping("/{id}/deactivate")
    public ApiResponse<ProductListItemDTO> deactivate(@PathVariable Long id) {
        return ApiResponse.ok(adminMenu.deactivateProduct(id), "Producto desactivado");
    }
}