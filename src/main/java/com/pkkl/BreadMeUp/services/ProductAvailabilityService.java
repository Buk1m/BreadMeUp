package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.model.ProductAvailability;

import java.time.LocalDate;
import java.util.List;

public interface ProductAvailabilityService {

    ProductAvailability getById(int id);

    ProductAvailability update(final ProductAvailability productAvailability);

    ProductAvailability add(final ProductAvailability productAvailability);

    ProductAvailability getByDateAndProduct(final LocalDate date, final int productId);
}
