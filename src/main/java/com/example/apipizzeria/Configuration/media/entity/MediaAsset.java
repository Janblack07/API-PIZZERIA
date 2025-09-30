package com.example.apipizzeria.Configuration.media.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity @Table(name = "media_assets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MediaAsset {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String publicId;

    @Column(nullable = false, length = 512)
    private String secureUrl;

    private String format;        // jpg, png, webp
    private String resourceType;  // image
    private Integer bytes;
    private Integer width;
    private Integer height;
    private String folder;

    @Column(columnDefinition = "datetime2")
    private Instant createdAt;

    @PrePersist void prePersist() { this.createdAt = Instant.now(); }
}