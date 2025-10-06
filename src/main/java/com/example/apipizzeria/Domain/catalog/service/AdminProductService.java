package com.example.apipizzeria.Domain.catalog.service;

import com.example.apipizzeria.Domain.catalog.dto.ProductDetailDTO;
import com.example.apipizzeria.Domain.catalog.dto.ProductListItemDTO;
import com.example.apipizzeria.Domain.catalog.dto.ProductUpsertRequest;
import com.example.apipizzeria.common.api.menu.dto.*;
import com.example.apipizzeria.common.enums.ProductType;
import org.springframework.data.domain.*;

public interface AdminProductService {
    ProductDetailDTO create(ProductUpsertRequest req);
    ProductDetailDTO update(Long id, ProductUpsertRequest req);
    void delete(Long id);                  // política: soft-delete -> active=false
    ProductDetailDTO setActive(Long id, boolean active);
    ProductDetailDTO get(Long id);         // admin puede ver aunque esté inactivo
    Page<ProductListItemDTO> list(ProductType type, String q, Boolean active, Pageable pageable);
}
