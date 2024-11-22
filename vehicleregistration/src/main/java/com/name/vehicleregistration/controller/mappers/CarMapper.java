package com.name.vehicleregistration.controller.mappers;

import com.name.vehicleregistration.model.Car;
import com.name.vehicleregistration.controller.dtos.CarResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CarMapper {

    private final BrandMapper brandMapper;

    // Constructor con todas las dependencias
    public CarMapper(BrandMapper brandMapper) {
        this.brandMapper = brandMapper;
    }

    public CarResponse toResponse(Car car) {
        return CarResponse.builder()
                .id(car.getId())
                .brand(brandMapper.toResponse(car.getBrand()))
                .model(car.getModel())
                .milleage(car.getMilleage())
                .price(car.getPrice())
                .modelYear(car.getModelYear())
                .description(car.getDescription())
                .colour(car.getColour())
                .fuelType(car.getFuelType())
                .numDoors(car.getNumDoors())
                .build();
    }
}