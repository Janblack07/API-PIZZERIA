package com.example.apipizzeria.Domain.catalog.entity;


import com.example.apipizzeria.Configuration.media.entity.MediaAsset;
import com.example.apipizzeria.common.BaseEntity;
import com.example.apipizzeria.common.enums.ProductType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name="products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product extends BaseEntity {

    @Column(nullable = false, length = 160)
    private String name;                // "Pizza Pepperoni"

    @Column(length = 400)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductType type;           // PIZZA, BEVERAGE, ...

    // Para items sin variantes
    @Column(precision = 10, scale = 2)
    private BigDecimal basePrice;

    private boolean active = true;

    private boolean customizable;       // builder (pizzas armables)
    /** Imagen principal del producto (opcional) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "featured_image_id")
    private MediaAsset featuredImage;

    /** Galería opcional (n imágenes) */
    @ManyToMany
    @JoinTable(
            name = "product_gallery",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    private Set<MediaAsset> gallery = new HashSet<>();

    // Variantes (tamaño/masa/precio)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductVariant> variants = new HashSet<>();

}