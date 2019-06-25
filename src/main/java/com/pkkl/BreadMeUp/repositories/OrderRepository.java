package com.pkkl.BreadMeUp.repositories;

import com.pkkl.BreadMeUp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
