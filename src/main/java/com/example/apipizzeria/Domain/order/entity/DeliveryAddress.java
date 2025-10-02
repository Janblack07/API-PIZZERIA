package com.example.apipizzeria.Domain.order.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DeliveryAddress {
    private String recipientName;
    private String phone;
    private String streetLine;
    private Double lat;
    private Double lng;
    private String notes;
}