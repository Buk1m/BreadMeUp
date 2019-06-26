package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class OrderPriceDto {
    double normalPrice;
    double discountedPrice;
}
