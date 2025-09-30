package com.example.apipizzeria.Configuration.media.service;


import com.example.apipizzeria.Configuration.cloudinary.CloudStorageService;
import com.example.apipizzeria.Configuration.media.dto.MediaDeleteResponse;
import com.example.apipizzeria.Configuration.media.dto.MediaUploadResponse;
import com.example.apipizzeria.Configuration.media.entity.MediaAsset;
import com.example.apipizzeria.Configuration.media.repository.MediaAssetRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class MediaServiceImpl implements MediaService {

    private final CloudStorageService storage;
    private final MediaAssetRepository repo;

    public MediaServiceImpl(CloudStorageService storage, MediaAssetRepository repo) {
        this.storage = storage;
        this.repo = repo;
    }

    @Override
    @Transactional
    public MediaUploadResponse upload(MultipartFile file, String folder) {
        Map<String, Object> res = storage.upload(file, folder);

        MediaAsset m = MediaAsset.builder()
                .publicId((String) res.get("public_id"))
                .secureUrl((String) res.get("secure_url"))
                .format((String) res.get("format"))
                .resourceType((String) res.get("resource_type"))
                .bytes((Integer) res.get("bytes"))
                .width((Integer) res.get("width"))
                .height((Integer) res.get("height"))
                .folder((String) res.get("folder"))
                .build();

        repo.save(m);
        return new MediaUploadResponse(m.getId(), m.getPublicId(), m.getSecureUrl(), m.getWidth(), m.getHeight(), m.getBytes());
    }

    @Override
    @Transactional
    public MediaDeleteResponse delete(String publicId) {
        storage.delete(publicId);
        repo.findByPublicId(publicId).ifPresent(repo::delete);
        return new MediaDeleteResponse(publicId, true);
    }
}