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
public class OrderCreateDto {
    private int userId;

    private int bakeryId;

    @NotNull(message = "Order's date cannot be null")
    private Date date;

    @NotNull(message = "Order's products list cannot be null")
    private Set<OrderProductDto> orderProducts;
}
