package com.example.apipizzeria.Domain.catalog.repository;

import com.example.apipizzeria.Domain.catalog.entity.Topping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToppingRepository extends JpaRepository<Topping, Long> {
    boolean existsByNameIgnoreCase(String name);
}