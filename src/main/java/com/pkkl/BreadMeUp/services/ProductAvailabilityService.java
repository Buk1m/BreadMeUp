package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.model.ProductAvailability;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDate;
import java.util.List;

public interface ProductAvailabilityService {

    ProductAvailability getById(int id);

    @PreAuthorize("hasRole('ROLE_MANAGER') && @methodSecurityExpression.isThisBakeryManager(principal, #productAvailability.product.bakery.id)")
    ProductAvailability update(final ProductAvailability productAvailability);

    @PreAuthorize("hasRole('ROLE_MANAGER') && @methodSecurityExpression.isThisBakeryManager(principal, #productAvailability.product.bakery.id)")
    ProductAvailability add(final ProductAvailability productAvailability);

    ProductAvailability getByDateAndProduct(final LocalDate date, final int productId);

    List<ProductAvailability> getAllByProduct(final int productId);
}
