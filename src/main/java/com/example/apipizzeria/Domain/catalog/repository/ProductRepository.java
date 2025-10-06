package com.example.apipizzeria.Domain.catalog.repository;


import com.example.apipizzeria.Domain.catalog.entity.Product;
import com.example.apipizzeria.common.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndActiveTrue(Long id);
    Page<Product> findByActiveTrue(Pageable pageable);
    Page<Product> findByActiveTrueAndType(ProductType type, Pageable pageable);
    Page<Product> findByActiveTrueAndNameContainingIgnoreCase(String q, Pageable pageable);
    Page<Product> findByActiveTrueAndTypeAndNameContainingIgnoreCase(ProductType t, String q, Pageable pageable);

    // Admin (incluye inactivos):
    Page<Product> findByType(ProductType type, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String q, Pageable pageable);
    Page<Product> findByTypeAndNameContainingIgnoreCase(ProductType t, String q, Pageable pageable);
}