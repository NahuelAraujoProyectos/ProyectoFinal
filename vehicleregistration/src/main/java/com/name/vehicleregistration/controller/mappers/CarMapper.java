package com.name.vehicleregistration.controller.mappers;

import com.name.vehicleregistration.entity.BrandEntity;
import com.name.vehicleregistration.exception.custom.car.BrandNotFoundException;
import com.name.vehicleregistration.model.Car;
import com.name.vehicleregistration.controller.dtos.CarRequest;
import com.name.vehicleregistration.controller.dtos.CarResponse;
import com.name.vehicleregistration.repository.BrandRepository;
import com.name.vehicleregistration.service.converters.BrandConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class CarMapper {

    private final BrandRepository brandRepository;
    private final BrandConverter brandConverter;
    private final BrandMapper brandMapper;

    // Constructor con todas las dependencias
    public CarMapper(BrandRepository brandRepository, BrandConverter brandConverter, BrandMapper brandMapper) {
        this.brandRepository = brandRepository;
        this.brandConverter = brandConverter;
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

    public Car toModel(CarRequest carRequest){
        Optional<BrandEntity> brandOptional = brandRepository.findById(carRequest.getBrandId());
        if (brandOptional.isEmpty()) {
            throw new BrandNotFoundException("Marca con ID " + carRequest.getBrandId() + " no encontrada.");
        }
        BrandEntity brandEntity = brandOptional.get();

        return Car.builder()
                .brand(brandConverter.toModel(brandEntity))
                .model(carRequest.getModel())
                .milleage(carRequest.getMilleage())
                .price(carRequest.getPrice())
                .modelYear(carRequest.getModelYear())
                .description(carRequest.getDescription())
                .colour(carRequest.getColour())
                .fuelType(carRequest.getFuelType())
                .numDoors(carRequest.getNumDoors())
                .build();
    }
}