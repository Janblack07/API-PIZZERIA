package com.example.apipizzeria.Domain.address.entity;


import com.example.apipizzeria.Domain.user.entity.User;
import com.example.apipizzeria.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "addresses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Address extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false, length = 160)
    private String label;          // "Casa", "Trabajo"

    @Column(nullable = false, length = 240)
    private String streetLine;     // texto completo de dirección

    @Column(length = 120)
    private String notes;          // referencia

    // Coordenadas opcionales (para delivery / zonas)
    private Double lat;
    private Double lng;

    private boolean favorite = false;
}