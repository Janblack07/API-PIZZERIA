package com.example.apipizzeria.Configuration.cloudinary;

import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface CloudStorageService {
    Map<String, Object> upload(MultipartFile file, String folder);
    void delete(String publicId);
    String buildUrl(String publicId, Integer width, Integer height, boolean crop, boolean autoFormat);
}