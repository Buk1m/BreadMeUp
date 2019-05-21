package com.pkkl.BreadMeUp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pkkl.BreadMeUp.services.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    private final JwtAuthSettings jwtAuthSettings;

    @Autowired
    public SecurityConfig(UserService userService, JwtAuthSettings jwtAuthSettings) {
        this.userService = userService;
        this.jwtAuthSettings = jwtAuthSettings;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtUsernameAndPasswordAuthenticationFilter jwtUsernameAndPasswordAuthenticationFilter =
                new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtAuthSettings);
        jwtUsernameAndPasswordAuthenticationFilter.setAuthenticationFailureHandler(this::authenticationFailureHandler);

        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(this.configureAuthenticationEntryPoint())
                .and()
                .addFilter(jwtUsernameAndPasswordAuthenticationFilter)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, jwtAuthSettings.getPath()).permitAll()
                .antMatchers(HttpMethod.POST, "/registration").permitAll()
                .anyRequest().authenticated();
    }

    private void authenticationFailureHandler(HttpServletRequest httpServletRequest,
                                              HttpServletResponse httpServletResponse, AuthenticationException e)
            throws IOException {
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpServletResponse.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(new AuthFailureMessage(e.getMessage())));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    private AuthenticationEntryPoint configureAuthenticationEntryPoint() {
        return (httpServletRequest, httpServletResponse, e)
                -> httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());

    }

    private class AuthFailureMessage {

        @Getter
        private final String message;

        private AuthFailureMessage(String message) {
            this.message = message;
        }
    }
}
