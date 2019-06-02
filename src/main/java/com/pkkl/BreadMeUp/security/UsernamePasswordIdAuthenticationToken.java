package com.pkkl.BreadMeUp.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class UsernamePasswordIdAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final long id;

    public UsernamePasswordIdAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, long id) {
        super(principal, credentials, authorities);
        this.id = id;
    }
}
