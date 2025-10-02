package com.example.apipizzeria.Domain.catalog.repository;


import com.example.apipizzeria.Domain.catalog.entity.Product;
import com.example.apipizzeria.common.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();
    List<Product> findByTypeAndActiveTrue(ProductType type);
}