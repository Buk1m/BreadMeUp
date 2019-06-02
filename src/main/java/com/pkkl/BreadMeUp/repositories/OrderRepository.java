package com.pkkl.BreadMeUp.repositories;

import com.pkkl.BreadMeUp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.cancelled=:cancelled WHERE o.order_id=:id")
    void setCancelled(@Param("id") int id, @Param("cancelled") boolean cancelled);
}
