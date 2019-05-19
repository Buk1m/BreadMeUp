package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class ClosedDaysDto {
    private int id;

    private Date date;
}
