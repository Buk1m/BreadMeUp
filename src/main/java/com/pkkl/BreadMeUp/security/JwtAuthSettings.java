package com.pkkl.BreadMeUp.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter

@Component
@PropertySource("classpath:auth.properties")
public class JwtAuthSettings {

    @Value("${auth.path}")
    private String path;

    @Value("${auth.header}")
    private String header;

    @Value("${auth.prefix}")
    private String prefix;

    @Value("${auth.expiration}")
    private int expiration;

    @Value("${auth.secret}")
    private String secret;
}

