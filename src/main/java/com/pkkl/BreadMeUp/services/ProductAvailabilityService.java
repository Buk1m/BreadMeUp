package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.model.ProductAvailability;

import java.time.LocalDate;
import java.util.List;

public interface ProductAvailabilityService {
    ProductAvailability getById(int id);
    List<ProductAvailability> getAll();
    void delete(int id);
    ProductAvailability update(final ProductAvailability productAvailability);
    ProductAvailability add(final ProductAvailability productAvailability);
    List<ProductAvailability> getByProduct(final int productId);
    List<ProductAvailability> getByDate(final LocalDate date);
}
