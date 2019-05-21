package com.pkkl.BreadMeUp.services;

import com.pkkl.BreadMeUp.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User register(User user);

}
