package com.pkkl.BreadMeUp.repositories;

import com.pkkl.BreadMeUp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
