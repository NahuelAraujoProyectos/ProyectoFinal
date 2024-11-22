package com.name.vehicleregistration.service.impl;

import com.name.vehicleregistration.controller.dtos.CarRequest;
import com.name.vehicleregistration.controller.mappers.BrandMapper;
import com.name.vehicleregistration.entity.BrandEntity;
import com.name.vehicleregistration.entity.CarEntity;
import com.name.vehicleregistration.exception.custom.car.BrandNotFoundException;
import com.name.vehicleregistration.exception.custom.car.CarNotFoundException;
import com.name.vehicleregistration.model.Brand;
import com.name.vehicleregistration.model.Car;
import com.name.vehicleregistration.repository.BrandRepository;
import com.name.vehicleregistration.repository.CarRepository;
import com.name.vehicleregistration.service.converters.BrandConverter;
import com.name.vehicleregistration.service.converters.CarConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
    @InjectMocks
    private CarServiceImpl carService;
    @Mock
    private CarRepository carRepository;
    @Mock
    private CarConverter carConverter;
    @Mock
    private BrandMapper brandMapper;
    @Mock
    private BrandConverter brandConverter;
    @Mock
    private BrandRepository brandRepository;

    @Test
    void addCar_test() {
        // Given
        Brand brand = Brand.builder()
                .id(1)
                .name("BrandName")
                .country("Country")
                .build();

        BrandEntity brandEntity = BrandEntity.builder()
                .id(1)
                .name("BrandName")
                .country("Country")
                .build();

        CarRequest carRequest = CarRequest.builder()
                .brandId(1)
                .model("Model X")
                .build();

        Car car = Car.builder()
                .brand(brand)
                .model("Model X")
                .build();

        CarEntity carEntity = CarEntity.builder()
                .brand(brandEntity)
                .model("Model X")
                .build();


        CarEntity carEntitySaved = CarEntity.builder()
                .id(1)
                .brand(brandEntity)
                .model("Model X")
                .build();

        // When
        when(brandRepository.findById(1)).thenReturn(Optional.of(brandEntity));
        when(carRepository.save(carEntity)).thenReturn(carEntitySaved);
        when(carConverter.toModel(carEntitySaved)).thenReturn(car);
        Car result = carService.addCar(carRequest);

        // Then
        assertNotNull(result);
        assertEquals("Model X", result.getModel());
    }


    @Test
    void addCar_ko() {
        // Given
        CarRequest carRequest = CarRequest.builder()
                .brandId(1)
                .build();

        // When
        when(brandRepository.findById(1)).thenReturn(Optional.empty());

        // Then
        BrandNotFoundException exception = assertThrows(BrandNotFoundException.class, () -> carService.addCar(carRequest));
        assertEquals("Marca con ID 1 no encontrada.", exception.getMessage());
    }

    @Test
    void getCarById_test() {
        // Given
        CarEntity carEntity = new CarEntity();
        carEntity.setId(1);
        carEntity.setModel("Model X");

        Car car = new Car();
        car.setId(1);
        car.setModel("Model X");

        // When
        when(carRepository.findById(1)).thenReturn(Optional.of(carEntity));
        when(carConverter.toModel(carEntity)).thenReturn(car);

        // Then
        Car result = carService.getCarById(1);
        assertEquals("Model X", result.getModel());
    }

    @Test
    void getCarById_ko() {
        // Given
        // When
        when(carRepository.findById(1)).thenReturn(Optional.empty());
        // Then
        CarNotFoundException exception = assertThrows(CarNotFoundException.class, () -> carService.getCarById(1));
    }

    @Test
    void updateCar_test() {
        // Given
        Brand brand = Brand.builder()
                .id(1)
                .name("BrandName")
                .country("Country")
                .build();

        CarRequest carRequest = CarRequest.builder()
                .brandId(1)
                .model("Updated Model")
                .build();

        Car car = new Car();
        car.setBrand(brand);
        car.setModel("Updated Model");

        CarEntity carEntity = CarEntity.builder().id(1).build();
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(1);

        // When
        when(carRepository.findById(1)).thenReturn(Optional.of(carEntity));
        when(brandRepository.findById(1)).thenReturn(Optional.of(brandEntity));
        when(carRepository.save(carEntity)).thenReturn(carEntity);
        when(carConverter.toModel(carEntity)).thenReturn(car);
        Car result = carService.updateCar(1, carRequest);

        // Then
        assertNotNull(result);
        assertEquals("Updated Model", result.getModel());
    }

    @Test
    void updateCar_ko() {
        // Given
        // When
        when(carRepository.findById(1)).thenReturn(Optional.empty());

        // Then
        CarNotFoundException exception = assertThrows(CarNotFoundException.class, () -> carService.updateCar(1,new CarRequest()));
    }

    @Test
    void updateCar_ko_BrandDoesNotExist() {
        // Given
        CarRequest carRequest = CarRequest.builder()
                .brandId(1)
                .model("Model X")
                .build();

        CarEntity carEntity = new CarEntity();
        carEntity.setId(1);

        // When
        when(carRepository.findById(1)).thenReturn(Optional.of(carEntity));
        when(brandRepository.findById(1)).thenReturn(Optional.empty());

        // Then
        BrandNotFoundException exception = assertThrows(BrandNotFoundException.class, () -> carService.updateCar(1,carRequest));

    }

    @Test
    void deleteById_test() {
        // Given
        CarEntity carEntity = CarEntity.builder().id(1).build();
        Car car = new Car();
        car.setModel("Model X");

        // When
        when(carRepository.findById(1)).thenReturn(Optional.of(carEntity));
        when(carConverter.toModel(carEntity)).thenReturn(car);
        Car result = carService.deleteById(1);

        // Then
        assertNotNull(result);
        assertEquals("Model X", result.getModel());
    }

    @Test
    void deteleById_ko() {
        // Given
        // When
        when(carRepository.findById(1)).thenReturn(Optional.empty());

        // Then
        CarNotFoundException exception = assertThrows(CarNotFoundException.class, () -> carService.deleteById(1));
    }

}
