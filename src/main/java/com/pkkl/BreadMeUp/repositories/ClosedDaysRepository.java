package com.pkkl.BreadMeUp.repositories;

import com.pkkl.BreadMeUp.model.ClosedDays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClosedDaysRepository extends JpaRepository<ClosedDays, Integer> {
}
