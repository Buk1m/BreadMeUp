package com.pkkl.BreadMeUp.dtos;

import com.pkkl.BreadMeUp.model.UnitOfMeasurement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProductDetailsDto {
    private int id;

    @NotBlank(message = "Order product name cannot be blank")
    @Length(min = 3, max = 64, message = "Order product name length should be from 3 to 64")
    private String productName;

    @Min(0)
    private double price;

    @Min(1)
    private int amount;

    @NotNull
    private UnitOfMeasurement unitOfMeasurement;

    @Min(0)
    private double size;

}
