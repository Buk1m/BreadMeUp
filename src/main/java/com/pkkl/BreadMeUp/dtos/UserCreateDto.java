package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@Builder
public class UserCreateDto {

    @Length(min = 3, max = 64, message = "Login length should be from 3 to 64")
    private String login;

    @Length(min = 8, max = 64, message = "Password length should be from 8 to 64")
    private String password;

    @Email(message = "Email is not valid")
    @Length(max = 64, message = "Email length should be to 64")
    private String email;

    @Pattern(regexp = "\\d{7,15}", message = "Phone is not valid")
    private String phone;
}
