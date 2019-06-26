package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.dtos.BakeryLocationDto;
import com.pkkl.BreadMeUp.model.Bakery;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface BakeryService {
    Bakery getById(int id);

    List<Bakery> getAll();

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(int id);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Bakery update(Bakery bakery);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Bakery add(Bakery bakery);

    BakeryLocationDto getLocation(int id);
}
