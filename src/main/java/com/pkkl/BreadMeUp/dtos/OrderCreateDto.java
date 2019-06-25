package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreateDto {
    @NotNull(message = "Bakery cannot be null")
    private BakeryDto bakery;

    @NotNull(message = "Order's date cannot be null")
    @FutureOrPresent
    private LocalDate date;

    @NotNull(message = "Order's products list cannot be null")
    private Set<OrderProductDto> orderProducts;
}
