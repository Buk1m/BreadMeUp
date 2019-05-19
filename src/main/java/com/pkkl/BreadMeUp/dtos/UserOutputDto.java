package com.pkkl.BreadMeUp.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserOutputDto {

    private int id;

    private String login;

    private String email;

    private String phone;
}
