package com.pkkl.BreadMeUp.repositories;

import com.pkkl.BreadMeUp.model.Product;
import com.pkkl.BreadMeUp.model.ProductAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAvailabilityRepository extends JpaRepository<ProductAvailability, Integer> {

    List<ProductAvailability> findAllByProduct(Product product);
}
