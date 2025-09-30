package com.example.apipizzeria.Configuration.media.service;


import com.example.apipizzeria.Configuration.media.dto.MediaDeleteResponse;
import com.example.apipizzeria.Configuration.media.dto.MediaUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MediaService {
    MediaUploadResponse upload(MultipartFile file, String folder);
    MediaDeleteResponse delete(String publicId);
}