package com.pkkl.BreadMeUp.security;

import com.pkkl.BreadMeUp.security.AuthUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class MethodSecurityExpression {

    public boolean isThisBakeryManager(UserDetails principal, int bakeryId) {
        AuthUserDetails authUserDetails = (AuthUserDetails) principal;

        if (authUserDetails.getUser().getBakery() != null) {
            return bakeryId == authUserDetails.getUser().getBakery().getId();
        } else {
            return false;
        }
    }
}
