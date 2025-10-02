package com.example.apipizzeria.Domain.catalog.repository;


import com.example.apipizzeria.Domain.catalog.entity.ComboItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComboItemRepository extends JpaRepository<ComboItem, Long> { }
