package com.example.apipizzeria.Domain.user.repository;

import com.example.apipizzeria.Domain.user.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryAddressRepository extends JpaRepository<Address, Long> {

    Page<Address> findByUserId(Long userId, Pageable pageable);

    Page<Address> findByUserIdAndFavorite(Long userId, boolean favorite, Pageable pageable);

    Optional<Address> findByIdAndUserId(Long id, Long userId);

    List<Address> findByUserId(Long userId); // para limpiar favorite
}