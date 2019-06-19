package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.model.Bakery;
import com.pkkl.BreadMeUp.model.Order;
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
}
