package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class OrderProductDto {

    @NotNull(message = "Product cannot be null")
    private ProductDto product;

    @Min(1)
    private int amount;
}
