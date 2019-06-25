package com.pkkl.BreadMeUp.dtos;

import com.pkkl.BreadMeUp.model.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DiscountDto {

    @NotNull(message = "Discount type cannot be null")
    private Set<DiscountType> discountTypes;
}
