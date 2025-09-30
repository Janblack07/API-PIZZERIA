package com.example.apipizzeria.Configuration.media.controller;


import com.example.apipizzeria.Configuration.media.dto.MediaDeleteResponse;
import com.example.apipizzeria.Configuration.media.dto.MediaUploadResponse;
import com.example.apipizzeria.Configuration.media.service.MediaService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/media")
public class MediaController {

    private final MediaService media;

    public MediaController(MediaService media) { this.media = media; }

    @PreAuthorize("hasAuthority('TYPE_ADMIN') or hasAnyAuthority('CASHIER','COOK')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MediaUploadResponse upload(@RequestPart("file") MultipartFile file,
                                      @RequestParam(defaultValue = "pizzeria/products") String folder) {
        return media.upload(file, folder);
    }

    @PreAuthorize("hasAuthority('TYPE_ADMIN')")
    @DeleteMapping("/{publicId}")
    public MediaDeleteResponse delete(@PathVariable String publicId) {
        return media.delete(publicId);
    }
}