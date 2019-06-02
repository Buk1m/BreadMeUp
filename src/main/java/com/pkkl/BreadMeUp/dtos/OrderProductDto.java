package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@Builder
public class OrderProductDto {
    private int id;

    private int productId;

    private long productVersion;

    @Min(0)
    private int amount;

    private long version;
}
