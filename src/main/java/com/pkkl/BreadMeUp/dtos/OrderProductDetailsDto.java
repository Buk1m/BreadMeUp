package com.pkkl.BreadMeUp.dtos;

import com.pkkl.BreadMeUp.model.UnitOfMeasurement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProductDetailsDto {
    private int id;

    private String productName;

    @Min(0)
    private double price;

    @Min(1)
    private int amount;

    private UnitOfMeasurement unitOfMeasurement;

    @Min(0)
    private double size;
}
