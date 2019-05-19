package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@Builder
public class RoleDto {

    private int id;

    @Length(min = 3, max = 20, message = "Name length should be from 3 to 20")
    private String name;
}
