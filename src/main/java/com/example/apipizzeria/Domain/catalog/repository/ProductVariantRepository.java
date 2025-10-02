package com.example.apipizzeria.Domain.catalog.repository;


import com.example.apipizzeria.Domain.catalog.entity.ProductVariant;
import com.example.apipizzeria.common.enums.Dough;
import com.example.apipizzeria.common.enums.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    Optional<ProductVariant> findByProductIdAndSizeAndDough(Long productId, Size size,
                                                            Dough dough);
}
