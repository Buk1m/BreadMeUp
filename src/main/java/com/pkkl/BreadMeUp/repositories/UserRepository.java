package com.pkkl.BreadMeUp.repositories;

import com.pkkl.BreadMeUp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.login = (:login)")
    Optional<User> findByLogin(@Param("login") String login);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.blocked=:blocked WHERE u.login=:login")
    void setBlocked(@Param("login") String login, @Param("blocked") boolean blocked);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.bakery=:bakeryId WHERE u.id=:id")
    void assignBakeryToUser(@Param("id") Integer id, @Param("bakeryId") int bakeryId);
}
