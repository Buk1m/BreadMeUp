package com.pkkl.BreadMeUp.security;

import lombok.Data;

@Data
class UserCredentials {
    private String login;
    private String password;
}