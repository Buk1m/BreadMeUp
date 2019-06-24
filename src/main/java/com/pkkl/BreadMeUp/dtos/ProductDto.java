package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private int id;

    @NotBlank(message = "Name cannot be blank")
    @Length(min = 3, max = 64, message = "Name length should be from 3 to 64")
    private String name;

    @Min(0)
    private double price;

    @Min(0)
    private int limit;

    private boolean active;

    @NotNull(message = "Bakery cannot be null")
    private BakeryDto bakery;

    @NotNull(message = "Category cannot be null")
    private CategoryDto category;

    @NotNull(message = "ProductType cannot be null")
    private ProductTypeDto productType;

    private List<ProductAvailabilityDto> productAvailability;

    private int version;
}
