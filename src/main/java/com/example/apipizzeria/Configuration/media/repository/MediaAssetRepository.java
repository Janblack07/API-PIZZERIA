package com.example.apipizzeria.Configuration.media.repository;


import com.example.apipizzeria.Configuration.media.entity.MediaAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaAssetRepository extends JpaRepository<MediaAsset, Long> {
    Optional<MediaAsset> findByPublicId(String publicId);
}