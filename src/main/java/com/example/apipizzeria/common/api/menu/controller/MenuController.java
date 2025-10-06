package com.example.apipizzeria.common.api.menu.controller;

import com.example.apipizzeria.Domain.catalog.dto.ProductDetailDTO;
import com.example.apipizzeria.Domain.catalog.dto.ProductListItemDTO;
import com.example.apipizzeria.common.api.ApiPage;
import com.example.apipizzeria.common.api.ApiPageUtil;
import com.example.apipizzeria.common.api.ApiResponse;
import com.example.apipizzeria.common.api.menu.service.MenuService;
import com.example.apipizzeria.common.enums.ProductType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Menú (Público)", description = "Consulta de productos visibles en el catálogo")
@RestController
@RequestMapping("/menu/products")
public class MenuController {

    private final MenuService menu;

    public MenuController(MenuService menu) {
        this.menu = menu;
    }

    @Operation(
            summary = "Listar productos del menú",
            description = "Retorna productos activos. Filtros opcionales por tipo (PIZZA, BEVERAGE, DESSERT, COMBO) y búsqueda por nombre."
    )
    @GetMapping
    public ApiResponse<ApiPage<ProductListItemDTO>> list(
            @RequestParam(required = false) ProductType type,
            @RequestParam(required = false) String q,
            @ParameterObject Pageable pageable
    ) {
        Page<ProductListItemDTO> page = menu.listProducts(type, q, pageable);
        ApiPage<ProductListItemDTO> body = ApiPageUtil.meta(page);
        return ApiResponse.ok(body, "Productos del menú");
    }

    @Operation(
            summary = "Detalle de producto",
            description = "Detalle de un producto activo del menú, incluyendo variantes e imágenes."
    )
    @GetMapping("/{id}")
    public ApiResponse<ProductDetailDTO> get(@PathVariable Long id) {
        ProductDetailDTO dto = menu.getProduct(id);
        return ApiResponse.ok(dto, "Detalle de producto");
    }
}