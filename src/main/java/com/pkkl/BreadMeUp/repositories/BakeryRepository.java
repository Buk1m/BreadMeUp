package com.pkkl.BreadMeUp.repositories;

import com.pkkl.BreadMeUp.model.Bakery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BakeryRepository extends JpaRepository<Bakery, Integer> {
}
