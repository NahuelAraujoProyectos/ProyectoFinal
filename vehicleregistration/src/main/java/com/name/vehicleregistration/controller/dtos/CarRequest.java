package com.name.vehicleregistration.controller.dtos;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CarRequest {
    private Integer brandId;
    private String model;
    private Integer milleage;
    private Double price;
    private Integer modelYear;
    private String description;
    private String colour;
    private String fuelType;
    private Integer numDoors;

}
