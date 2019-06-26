package com.pkkl.BreadMeUp.dtos;

import com.pkkl.BreadMeUp.model.UnitOfMeasurement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductTypeDto {
    private int id;

    @NotNull(message = "Unit cannot be null")
    private UnitOfMeasurement unitOfMeasurement;

    @Min(0)
    private double size;
}
