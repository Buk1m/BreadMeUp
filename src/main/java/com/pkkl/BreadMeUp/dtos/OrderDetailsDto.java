package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailsDto {

    private int id;

    private BakeryDto bakery;

    private UserDetailsDto user;

    private LocalDate date;

//    private Set<OrderProductDetailsDto> orderProducts;

    private boolean completed;

    private boolean cancelled;
}
