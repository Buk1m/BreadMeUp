package com.pkkl.BreadMeUp.repositories;

import com.pkkl.BreadMeUp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
