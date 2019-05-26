package com.pkkl.BreadMeUp.security;

import com.pkkl.BreadMeUp.model.Role;
import com.pkkl.BreadMeUp.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class AuthUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(StringUtils.join(
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList()), ','));
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.user.isBlocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Integer getId() {
        return this.user.getId();
    }
}
