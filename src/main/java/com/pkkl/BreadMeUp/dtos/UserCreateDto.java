package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDto {

    @Length(min = 3, max = 64, message = "Login length should be from 3 to 64")
    @NotBlank(message = "Login cannot be blank")
    private String login;

    @Length(min = 8, max = 64, message = "Password length should be from 8 to 64")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Email(message = "Email is not valid")
    @Length(max = 64, message = "Email length should be to 64")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Pattern(regexp = "\\d{7,15}", message = "Phone is not valid")
    @NotBlank(message = "Phone cannot be blank")
    private String phone;

    private BakeryDto bakery;
}
