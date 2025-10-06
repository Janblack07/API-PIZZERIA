package com.example.apipizzeria.Domain.catalog.entity;


import com.example.apipizzeria.Configuration.media.entity.MediaAsset;
import com.example.apipizzeria.common.BaseEntity;
import com.example.apipizzeria.common.enums.Dough;
import com.example.apipizzeria.common.enums.Size;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name="product_variants",
        uniqueConstraints = @UniqueConstraint(name="ux_product_size_dough", columnNames = {"product_id","size","dough"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductVariant extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @Enumerated(EnumType.STRING) @Column(length=10)
    private Size size;                  // nullable si no aplica

    @Enumerated(EnumType.STRING) @Column(length=10)
    private Dough dough;               // nullable si no aplica

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private MediaAsset image;
}