package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class OrderDetailsDto {

    private int id;

    @NotNull
    private BakeryDto bakery;

    @NotNull
    private UserDetailsDto user;

    @NotNull
    private Date date;

    @NotNull
    private Set<OrderProductDetailsDto> orderProducts;

    private boolean completed;
}
