package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class OrderBasicDto {
    private int id;

    @NotBlank(message = "Bakery name cannot be blank")
    private String bakeryName;

    @NotBlank(message = "Username cannot be blank")
    private String userName;

    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotNull(message = "Order receive date cannot be null")
    private Date orderReceiveDate;

    private boolean completed;
}
