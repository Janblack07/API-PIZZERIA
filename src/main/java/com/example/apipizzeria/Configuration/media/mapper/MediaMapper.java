package com.example.apipizzeria.Configuration.media.mapper;

import com.example.apipizzeria.Configuration.media.entity.MediaAsset;
import com.example.apipizzeria.common.api.menu.dto.MediaDTO;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class MediaMapper {
    private MediaMapper() {}

    public static MediaDTO toDTO(MediaAsset m) {
        if (m == null) return null;
        // alt básico = publicId; si luego guardas alt/label, cámbialo aquí
        return new MediaDTO(m.getSecureUrl(), m.getPublicId());
    }

    public static List<MediaDTO> toDTOList(Collection<MediaAsset> media) {
        if (media == null || media.isEmpty()) return List.of();
        return media.stream()
                .filter(Objects::nonNull)
                .map(MediaMapper::toDTO)
                .toList();
    }
}