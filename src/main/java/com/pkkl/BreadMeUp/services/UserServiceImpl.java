package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.exceptions.ConstraintException;
import com.pkkl.BreadMeUp.exceptions.DatabaseException;
import com.pkkl.BreadMeUp.model.Role;
import com.pkkl.BreadMeUp.model.User;
import com.pkkl.BreadMeUp.repositories.RoleRepository;
import com.pkkl.BreadMeUp.repositories.UserRepository;
import com.pkkl.BreadMeUp.security.AuthUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.Set;

@Slf4j

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public User register(User user) {
        try {
            Role role = this.roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Role does not exists"));
            user.setRoles(Set.of(role));
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));
            return this.userRepository.save(user);
        } catch (InvalidDataAccessApiUsageException e) {
            throw new ConstraintException("User already exists", e);
        } catch (ConstraintViolationException e) {
            throw new ConstraintException(e.getConstraintViolations().toString(), e);
        } catch (Exception e) {
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                throw new ConstraintException(e.getMessage(), e);
            }
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = this.userRepository.findByLogin(username)
                    .orElseThrow(RuntimeException::new);
            return new AuthUserDetails(user);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not exists with login=" + username);
        }
    }
}
