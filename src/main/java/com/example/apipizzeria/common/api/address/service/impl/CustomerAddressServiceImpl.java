package com.example.apipizzeria.common.api.address.service.impl;

import com.example.apipizzeria.Domain.user.entity.Address;
import com.example.apipizzeria.Domain.user.entity.User;
import com.example.apipizzeria.Domain.user.repository.DeliveryAddressRepository;
import com.example.apipizzeria.Domain.user.repository.UserRepository;
import com.example.apipizzeria.common.api.address.dto.*;
import com.example.apipizzeria.common.api.address.mapper.AddressMapper;
import com.example.apipizzeria.common.api.address.service.CustomerAddressService;
import com.example.apipizzeria.common.exception.NotFoundException;
import com.example.apipizzeria.common.security.CurrentUser;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerAddressServiceImpl implements CustomerAddressService {

    private final DeliveryAddressRepository addrRepo;
    private final UserRepository userRepo;

    public CustomerAddressServiceImpl(DeliveryAddressRepository addrRepo, UserRepository userRepo) {
        this.addrRepo = addrRepo;
        this.userRepo = userRepo;
    }

    private User me() {
        String email = CurrentUser.email();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AddressResponse> listMine(Boolean onlyFavorites, Pageable pageable) {
        Long uid = me().getId();
        Page<Address> page = (onlyFavorites != null && onlyFavorites)
                ? addrRepo.findByUserIdAndFavorite(uid, true, pageable)
                : addrRepo.findByUserId(uid, pageable);
        return page.map(AddressMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getMine(Long id) {
        Long uid = me().getId();
        Address a = addrRepo.findByIdAndUserId(id, uid)
                .orElseThrow(() -> new NotFoundException("Dirección no encontrada"));
        return AddressMapper.toDTO(a);
    }

    @Override
    public AddressResponse createMine(AddressCreateRequest request) {
        User u = me();

        Address a = new Address();
        AddressMapper.applyCreate(a, request);
        a.setUser(u);

        // favorite: si viene true, desmarcamos el resto
        if (request.favorite()) {
            addrRepo.findByUserId(u.getId()).forEach(ad -> ad.setFavorite(false));
            a.setFavorite(true);
        }

        a = addrRepo.save(a);
        return AddressMapper.toDTO(a);
    }

    @Override
    public AddressResponse updateMine(Long id, AddressUpdateRequest request) {
        Long uid = me().getId();
        Address a = addrRepo.findByIdAndUserId(id, uid)
                .orElseThrow(() -> new NotFoundException("Dirección no encontrada"));
        AddressMapper.applyUpdate(a, request);
        return AddressMapper.toDTO(a);
    }

    @Override
    public AddressResponse setFavoriteMine(Long id) {
        User u = me();
        Address a = addrRepo.findByIdAndUserId(id, u.getId())
                .orElseThrow(() -> new NotFoundException("Dirección no encontrada"));

        // desmarcar todas y marcar la elegida
        List<Address> all = addrRepo.findByUserId(u.getId());
        all.forEach(ad -> ad.setFavorite(false));
        a.setFavorite(true);

        return AddressMapper.toDTO(a);
    }

    @Override
    public void deleteMine(Long id) {
        Long uid = me().getId();
        Address a = addrRepo.findByIdAndUserId(id, uid)
                .orElseThrow(() -> new NotFoundException("Dirección no encontrada"));

        // Si tu BaseEntity NO trae "active", puedes borrar físico:
        addrRepo.delete(a);

        // Si tu BaseEntity trae soft-delete, cámbialo por:
        // a.setActive(false);
        // a.setFavorite(false);
    }
}