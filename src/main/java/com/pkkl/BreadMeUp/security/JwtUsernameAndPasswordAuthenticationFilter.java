package com.pkkl.BreadMeUp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authManager;

    private JwtAuthSettings jwtAuthSettings;

    JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager, JwtAuthSettings jwtAuthSettings) {
        this.authManager = authManager;
        this.jwtAuthSettings = jwtAuthSettings;

        super.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher(jwtAuthSettings.getPath(), "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            UserCredentials userCredentials =
                    new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userCredentials.getLogin(), userCredentials.getPassword(), Collections.emptyList());

            return authManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) {
        long now = System.currentTimeMillis();
        AuthUserDetails authUserDetails = (AuthUserDetails) auth.getPrincipal();
        String token = Jwts.builder()
                .setSubject(authUserDetails.getUsername())
                .claim("authorities", auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .claim("userId", authUserDetails.getId())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtAuthSettings.getExpiration() * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtAuthSettings.getSecret().getBytes())
                .compact();

        response.addHeader(jwtAuthSettings.getHeader(), jwtAuthSettings.getPrefix() + ' ' + token);
    }
}
