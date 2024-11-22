package com.name.vehicleregistration.controller.dtos;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BrandRequest {
    private String name;
    private String country;
}
