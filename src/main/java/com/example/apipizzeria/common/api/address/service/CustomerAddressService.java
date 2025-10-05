package com.example.apipizzeria.common.api.address.service;

import com.example.apipizzeria.common.api.address.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerAddressService {
    Page<AddressResponse> listMine(Boolean onlyFavorites, Pageable pageable);
    AddressResponse getMine(Long id);
    AddressResponse createMine(AddressCreateRequest request);
    AddressResponse updateMine(Long id, AddressUpdateRequest request);
    AddressResponse setFavoriteMine(Long id);
    void deleteMine(Long id); // si prefieres soft-delete, aquí lo aplicas
}
