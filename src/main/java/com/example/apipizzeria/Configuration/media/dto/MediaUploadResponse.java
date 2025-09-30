package com.example.apipizzeria.Configuration.media.dto;

public record MediaUploadResponse(
        Long id,
        String publicId,
        String secureUrl,
        Integer width,
        Integer height,
        Integer bytes
) {}