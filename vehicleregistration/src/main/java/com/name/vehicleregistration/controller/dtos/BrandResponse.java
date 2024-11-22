package com.name.vehicleregistration.controller.dtos;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {
    private Integer id;
    private String name;
    private String country;
}
