package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductAvailabilityDto {
    private int id;

    @NotNull
    private ProductDto product;

    @NotNull
    private LocalDate date;

    @Min(0)
    private int orderedNumber;

    @Min(0)
    private int limit;
}
