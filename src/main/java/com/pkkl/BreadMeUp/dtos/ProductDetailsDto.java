package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailsDto {

    private int id;

    private String name;

    private double price;

    private int limit;

    private boolean active;

    private BakeryDto bakery;

    private CategoryDto category;

    private ProductTypeDto productType;

    private int version;
}
