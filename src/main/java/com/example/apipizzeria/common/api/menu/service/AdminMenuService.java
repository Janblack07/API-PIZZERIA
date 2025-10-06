package com.example.apipizzeria.common.api.menu.service;


import com.example.apipizzeria.Domain.catalog.dto.ProductListItemDTO;

public interface AdminMenuService {
    /** Activa un producto para que aparezca en /menu */
    ProductListItemDTO activateProduct(Long productId);

    /** Desactiva un producto para ocultarlo del /menu (no borra en admin) */
    ProductListItemDTO deactivateProduct(Long productId);
}
