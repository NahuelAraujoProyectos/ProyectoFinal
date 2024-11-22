package com.name.vehicleregistration.service.converters;

import com.name.vehicleregistration.model.Car;
import com.name.vehicleregistration.entity.CarEntity;
import org.springframework.stereotype.Component;

@Component
public class CarConverter {

    private final BrandConverter brandConverter;

    public CarConverter(BrandConverter brandConverter) {
        this.brandConverter = brandConverter;
    }

    // Convierte el modelo Car a la entidad CarEntity
    public CarEntity toEntity(Car car) {
        return CarEntity.builder()
                // Convertir el modelo Brand a la entidad BrandEntity antes de asignarlo
                .brand(brandConverter.toEntity(car.getBrand()))
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

    // Convierte la entidad CarEntity al modelo Car
    public Car toModel(CarEntity carEntity) {
        return Car.builder()
                .id(carEntity.getId())
                // Convertir la entidad BrandEntity al modelo Brand
                .brand(brandConverter.toModel(carEntity.getBrand()))
                .model(carEntity.getModel())
                .milleage(carEntity.getMilleage())
                .price(carEntity.getPrice())
                .modelYear(carEntity.getModelYear())
                .description(carEntity.getDescription())
                .colour(carEntity.getColour())
                .fuelType(carEntity.getFuelType())
                .numDoors(carEntity.getNumDoors())
                .build();
    }
}
