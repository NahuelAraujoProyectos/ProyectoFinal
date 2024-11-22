package com.name.vehicleregistration.service.converters;

import com.name.vehicleregistration.entity.BrandEntity;
import com.name.vehicleregistration.entity.CarEntity;
import com.name.vehicleregistration.model.Brand;
import com.name.vehicleregistration.model.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarConverterTest {

    @InjectMocks
    private CarConverter carConverter;

    @Mock
    private BrandConverter brandConverter;

    @Test
    void toEntity_test() {
        // Given: Crear un objeto Car
        Brand brand = Brand.builder()
                .id(1)
                .name("Toyota")
                .country("Japan")
                .build();

        Car car = Car.builder()
                .brand(brand)
                .model("Corolla")
                .milleage(100000)
                .price(15000.0)
                .modelYear(2015)
                .description("Sedan de 4 puertas")
                .colour("Rojo")
                .fuelType("Gasolina")
                .numDoors(4)
                .build();

        // Crear la entidad BrandEntity mockeada
        BrandEntity brandEntity = BrandEntity.builder()
                .id(1)
                .name("Toyota")
                .country("Japan")
                .build();

        // Simular el comportamiento de brandConverter
        when(brandConverter.toEntity(brand)).thenReturn(brandEntity);

        // When: Convertir el objeto Car a CarEntity
        CarEntity carEntity = carConverter.toEntity(car);

        // Then: Verificar que los valores sean los esperados
        assertEquals(brandEntity, carEntity.getBrand());
        assertEquals(car.getModel(), carEntity.getModel());
        assertEquals(car.getMilleage(), carEntity.getMilleage());
        assertEquals(car.getPrice(), carEntity.getPrice());
        assertEquals(car.getModelYear(), carEntity.getModelYear());
        assertEquals(car.getDescription(), carEntity.getDescription());
        assertEquals(car.getColour(), carEntity.getColour());
        assertEquals(car.getFuelType(), carEntity.getFuelType());
        assertEquals(car.getNumDoors(), carEntity.getNumDoors());
    }

    @Test
    void toModel_test() {
        // Given: Crear una entidad CarEntity
        BrandEntity brandEntity = BrandEntity.builder()
                .id(1)
                .name("Toyota")
                .country("Japan")
                .build();

        CarEntity carEntity = CarEntity.builder()
                .id(1)
                .brand(brandEntity)
                .model("Corolla")
                .milleage(100000)
                .price(15000.0)
                .modelYear(2015)
                .description("Sedan de 4 puertas")
                .colour("Rojo")
                .fuelType("Gasolina")
                .numDoors(4)
                .build();

        // Crear el modelo Brand mockeado
        Brand brand = Brand.builder()
                .id(1)
                .name("Toyota")
                .country("Japan")
                .build();

        // Simular el comportamiento de brandConverter
        when(brandConverter.toModel(brandEntity)).thenReturn(brand);

        // When: Convertir la entidad CarEntity a Car
        Car car = carConverter.toModel(carEntity);

        // Then: Verificar que los valores sean los esperados
        assertEquals(carEntity.getId(), car.getId());
        assertEquals(brand, car.getBrand());
        assertEquals(carEntity.getModel(), car.getModel());
        assertEquals(carEntity.getMilleage(), car.getMilleage());
        assertEquals(carEntity.getPrice(), car.getPrice());
        assertEquals(carEntity.getModelYear(), car.getModelYear());
        assertEquals(carEntity.getDescription(), car.getDescription());
        assertEquals(carEntity.getColour(), car.getColour());
        assertEquals(carEntity.getFuelType(), car.getFuelType());
        assertEquals(carEntity.getNumDoors(), car.getNumDoors());
    }
}