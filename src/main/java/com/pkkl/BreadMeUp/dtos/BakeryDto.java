package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BakeryDto {
    private int id;

    @NotBlank
    private String placeId;

    @NotBlank
    @Length(min = 3, max = 64, message = "Name length should be from 3 to 64")
    private String name;

    @NotBlank
    @Length(min = 3, max = 64, message = "City length should be from 3 to 64")
    private String city;

    @NotBlank
    @Pattern(regexp = "\\d{2}-\\d{3}", message = "Postal code is not valid")
    private String postalCode;

    @NotBlank
    @Length(min = 3, max = 64, message = "Street name length should be from 3 to 64")
    private String streetName;

    @NotBlank
    @Length(min = 1, max = 15, message = "Street number length should be from 1 to 15")
    private String streetNumber;
}
