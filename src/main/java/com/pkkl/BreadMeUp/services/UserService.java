package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User register(User user);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    User registerManager(User user);

    @PreAuthorize("hasRole('ROLE_ADMIN') && !principal.username.equals(#login)")
    void blockUser(String login);

    @PreAuthorize("hasRole('ROLE_ADMIN') && !principal.username.equals(#login)")
    void unblockUser(String login);

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    void assignBakeryToUser(Integer userId, int bakeryId);
}
