package com.example.apipizzeria.common.api.menu.service.impl;

import com.example.apipizzeria.Domain.catalog.dto.ProductListItemDTO;
import com.example.apipizzeria.Domain.catalog.entity.Product;
import com.example.apipizzeria.Domain.catalog.mapper.ProductMapper;
import com.example.apipizzeria.Domain.catalog.repository.ProductRepository;
import com.example.apipizzeria.common.api.menu.service.AdminMenuService;
import com.example.apipizzeria.common.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminMenuServiceImpl implements AdminMenuService {

    private final ProductRepository productRepo;

    public AdminMenuServiceImpl(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public ProductListItemDTO activateProduct(Long productId) {
        Product p = productRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException("Producto no existe"));
        if (!p.isActive()) {
            p.setActive(true);
        }
        return ProductMapper.toListItem(p);
    }

    @Override
    public ProductListItemDTO deactivateProduct(Long productId) {
        Product p = productRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException("Producto no existe"));
        if (p.isActive()) {
            p.setActive(false);
        }
        return ProductMapper.toListItem(p);
    }
}
