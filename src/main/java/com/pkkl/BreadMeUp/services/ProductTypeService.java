package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.model.ProductType;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.security.RolesAllowed;
import java.util.List;

public interface ProductTypeService {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ProductType getById(int id);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<ProductType> getAll();

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void delete(int id);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ProductType update(final ProductType productType);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ProductType add(final ProductType productType);
}
