package com.example.apipizzeria.Domain.user.repository;


import com.example.apipizzeria.Domain.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserId(Long userId);
}