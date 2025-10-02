package com.example.apipizzeria.Domain.catalog.repository;

import com.example.apipizzeria.Domain.catalog.entity.ProductAllowedTopping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAllowedToppingRepository extends JpaRepository<ProductAllowedTopping, Long> { }
