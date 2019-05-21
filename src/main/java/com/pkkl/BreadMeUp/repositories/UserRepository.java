package com.pkkl.BreadMeUp.repositories;

import com.pkkl.BreadMeUp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.login = (:login)")
    Optional<User> findByLogin(@Param("login") String login);
}
