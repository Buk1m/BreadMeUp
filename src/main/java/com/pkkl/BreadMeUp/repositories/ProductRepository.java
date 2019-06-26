package com.pkkl.BreadMeUp.repositories;

import com.pkkl.BreadMeUp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
