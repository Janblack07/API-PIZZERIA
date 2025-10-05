package com.example.apipizzeria.common.api.address.dto;

import jakarta.validation.constraints.*;

public record AddressCreateRequest(
        @NotBlank @Size(max = 160) String label,
        @NotBlank @Size(max = 240) String streetLine,
        @Size(max = 120) String notes,
        Double lat,
        Double lng,
        boolean favorite   // si llega true, se marca y se desmarcan las demás
) {}
