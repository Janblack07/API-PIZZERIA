package com.example.apipizzeria.common.api.address.mapper;

import com.example.apipizzeria.Domain.user.entity.Address;
import com.example.apipizzeria.common.api.address.dto.*;

public final class AddressMapper {
    private AddressMapper(){}

    public static AddressResponse toDTO(Address a) {
        return new AddressResponse(
                a.getId(),
                a.getLabel(),
                a.getStreetLine(),
                a.getNotes(),
                a.getLat(),
                a.getLng(),
                a.isFavorite()
        );
    }

    public static void applyCreate(Address a, AddressCreateRequest r) {
        a.setLabel(r.label());
        a.setStreetLine(r.streetLine());
        a.setNotes(r.notes());
        a.setLat(r.lat());
        a.setLng(r.lng());
        a.setFavorite(false); // lo decide el servicio
    }

    public static void applyUpdate(Address a, AddressUpdateRequest r) {
        a.setLabel(r.label());
        a.setStreetLine(r.streetLine());
        a.setNotes(r.notes());
        a.setLat(r.lat());
        a.setLng(r.lng());
    }
}
