package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleDto {

    private int id;

    @Length(min = 3, max = 20, message = "Name length should be from 3 to 20")
    @NotBlank(message = "Name cannot be blank")
    private String name;
}
