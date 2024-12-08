package com.name.vehicleregistration.service.impl;

import com.name.vehicleregistration.controller.dtos.CarRequest;
import com.name.vehicleregistration.controller.mappers.BrandMapper;
import com.name.vehicleregistration.entity.BrandEntity;
import com.name.vehicleregistration.entity.CarEntity;
import com.name.vehicleregistration.exception.car.BrandNotFoundException;
import com.name.vehicleregistration.exception.car.CarNotFoundException;
import com.name.vehicleregistration.exception.car.CsvFileException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    void carsCsv_test(){
        // Given
        BrandEntity brandEntity = BrandEntity.builder()
                .id(1)
                .name("TestBrand")
                .build();

        CarEntity carEntity = CarEntity.builder()
                .id(1)
                .brand(brandEntity)
                .model("Model X")
                .colour("Red")
                .description("Sedan")
                .modelYear(2020)
                .price(20000.0)
                .fuelType("Gasoline")
                .numDoors(4)
                .build();

        List<CarEntity> carList = List.of(carEntity);
        Page<CarEntity> carPage = new PageImpl<>(carList);

        // When
        when(carRepository.findAll(any(Pageable.class))).thenReturn(carPage);

        // Then
        String expectedCsv = """
                Brand,Model,Colour,Description,ModelYear,Price,FuelType,NumDoors
                TestBrand,Model X,Red,Sedan,2020,20000.0,Gasoline,4
                """;
        String actualCsv = carService.carsCsv();
        assertEquals(expectedCsv.trim(), actualCsv.trim());
    }

    @Test
    void uploadCars_success() throws Exception {
        // Given
        String csvContent = "brand,model,milleage,price,modelYear,description,colour,fuelType,numDoors\n" +
                "Toyota,Corolla,10000,20000,2020,Sedan,Red,Gasoline,4\n";

        MultipartFile file = new MockMultipartFile("file", "cars.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8));

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("Toyota");

        when(brandRepository.findByName("Toyota")).thenReturn(brandEntity);

        // When
        String result = carService.uploadCars(file);

        // Then
        assertEquals("Se han cargado y guardado 1 coches desde el archivo CSV.", result);
    }

    @Test
    void uploadCars_ko_brandNotFound() throws Exception {
        // Given
        String csvContent = "brand,model,milleage,price,modelYear,description,colour,fuelType,numDoors\n" +
                "Ford,Focus,20000,15000,2018,Hatchback,Blue,Diesel,4\n";

        MultipartFile file = new MockMultipartFile("file", "cars.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8));

        when(brandRepository.findByName("Ford")).thenReturn(null); // Simular marca no encontrada

        // When & Then
        BrandNotFoundException exception = assertThrows(BrandNotFoundException.class, () -> carService.uploadCars(file));
        assertEquals("Marca no encontrada: Ford", exception.getMessage());
    }

    @Test
    void uploadCars_ko_emptyFile() {
        // Given
        MultipartFile emptyFile = new MockMultipartFile("file", "cars.csv", "text/csv", new byte[0]); // Archivo vacío

        // When & Then
        CsvFileException exception = assertThrows(CsvFileException.class, () -> carService.uploadCars(emptyFile));
        assertEquals("El archivo está vacío.", exception.getMessage());
    }

    @Test
    void uploadCars_ko_invalidFileType() {
        // Given
        MultipartFile nonCsvFile = new MockMultipartFile("file", "cars.txt", "text/plain", "invalid content".getBytes(StandardCharsets.UTF_8));

        // When & Then
        CsvFileException exception = assertThrows(CsvFileException.class, () -> carService.uploadCars(nonCsvFile));
        assertEquals("El archivo no es un CSV válido.", exception.getMessage());
    }

    @Test
    void uploadCars_fail_ioException() throws IOException {
        // Given
        MultipartFile file = mock(MultipartFile.class); // Mockear un archivo para lanzar una IOException
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("cars.csv");
        when(file.getInputStream()).thenThrow(new IOException("Error al leer el archivo"));

        // When & Then
        CsvFileException exception = assertThrows(CsvFileException.class, () -> carService.uploadCars(file));
        assertEquals("No se pudo leer el archivo CSV", exception.getMessage());

    }



}
