package com.example.apipizzeria.common.api.address.dto;

public record AddressResponse(
        Long id,
        String label,
        String streetLine,
        String notes,
        Double lat,
        Double lng,
        boolean favorite
) {}
