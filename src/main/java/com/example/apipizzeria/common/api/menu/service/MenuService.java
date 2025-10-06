package com.example.apipizzeria.common.api.menu.service;


import com.example.apipizzeria.Domain.catalog.dto.ProductDetailDTO;
import com.example.apipizzeria.Domain.catalog.dto.ProductListItemDTO;
import com.example.apipizzeria.common.enums.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MenuService {


    Page<ProductListItemDTO> listProducts(ProductType type, String q, Pageable pageable);

    ProductDetailDTO getProduct(Long productId);
}

