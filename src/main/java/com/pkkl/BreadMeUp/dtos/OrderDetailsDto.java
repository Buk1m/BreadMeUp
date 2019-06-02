package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class OrderDetailsDto {

    private int id;

    private BakeryDto bakery;

    private UserDetailsDto user;

    private Date date;

    private Set<OrderProductDetailsDto> orderProducts;

    private boolean completed;
}
