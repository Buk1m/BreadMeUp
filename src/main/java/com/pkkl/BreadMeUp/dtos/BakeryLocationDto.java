package com.pkkl.BreadMeUp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BakeryLocationDto {

    private Result result;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Result {

        private Geometry geometry;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        class Geometry {

            private Location location;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            class Location {
                private double lat;
                private double lng;
            }
        }
    }
}
