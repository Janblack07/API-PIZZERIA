package com.example.apipizzeria.Configuration.media.dto;

public record MediaDeleteResponse(
        String publicId,
        boolean deleted
) {}