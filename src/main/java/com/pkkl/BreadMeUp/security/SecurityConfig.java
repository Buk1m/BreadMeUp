package com.pkkl.BreadMeUp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pkkl.BreadMeUp.services.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    private final JwtAuthSettings jwtAuthSettings;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(UserService userService, JwtAuthSettings jwtAuthSettings, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtAuthSettings = jwtAuthSettings;
        this.passwordEncoder = passwordEncoder;
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
                .addFilterAfter(new JwtTokenAuthenticationFilter(this.jwtAuthSettings, this.userService), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, jwtAuthSettings.getPath()).permitAll()
                .antMatchers(HttpMethod.POST, "/registration").permitAll()
                .antMatchers(HttpMethod.PUT, "/admin/users/**").hasRole("ADMIN")
                .antMatchers("/admin/types/**").hasRole("ADMIN")
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
        auth.userDetailsService(this.userService).passwordEncoder(this.passwordEncoder);
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
