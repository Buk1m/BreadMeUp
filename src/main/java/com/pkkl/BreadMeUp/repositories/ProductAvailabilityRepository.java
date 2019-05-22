package com.pkkl.BreadMeUp.repositories;

import com.pkkl.BreadMeUp.model.ProductAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAvailabilityRepository extends JpaRepository<ProductAvailability, Integer> {
}
