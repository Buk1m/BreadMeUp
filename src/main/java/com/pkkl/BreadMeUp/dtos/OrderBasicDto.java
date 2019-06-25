package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderBasicDto {
    private int id;

    @NotBlank(message = "Bakery name cannot be blank")
    private String bakeryName;

    @NotBlank(message = "Username cannot be blank")
    private String userLogin;

    @NotBlank(message = "City cannot be blank")
    private String bakeryCity;

    @NotNull(message = "Order receive date cannot be null")
    private LocalDate date;

    private boolean completed;

    private boolean cancelled;
}
