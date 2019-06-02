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

    @NotBlank
    private String bakeryName;

    @NotBlank
    private String userName;

    @NotBlank
    private String city;

    @NotNull
    private Date orderReceiveDate;

    private boolean completed;

}
