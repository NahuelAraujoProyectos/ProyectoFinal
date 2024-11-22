package com.name.vehicleregistration.service.impl;

import com.name.vehicleregistration.entity.BrandEntity;
import com.name.vehicleregistration.entity.CarEntity;
import com.name.vehicleregistration.exception.custom.car.*;
import com.name.vehicleregistration.model.Car;
import com.name.vehicleregistration.repository.BrandRepository;
import com.name.vehicleregistration.repository.CarRepository;
import com.name.vehicleregistration.service.CarService;
import com.name.vehicleregistration.service.converters.CarConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class CarServiceImpl implements CarService {

    final CarRepository carRepository;
    final CarConverter carConverter;
    final BrandRepository brandRepository;

    public CarServiceImpl(CarRepository carRepository, CarConverter carConverter, BrandRepository brandRepository) {
        this.carRepository = carRepository;
        this.carConverter = carConverter;
        this.brandRepository = brandRepository;
    }

    @Override
    public Car addCar(Car car) {
        BrandEntity brandEntity = brandRepository.findById(car.getBrand().getId())
                .orElseThrow(() -> new BrandNotFoundException("Marca con ID " + car.getBrand().getId() + " no encontrada."));

        // Crear el CarEntity usando el BrandEntity encontrado
        CarEntity carEntity = CarEntity.builder()
                .brand(brandEntity)
                .model(car.getModel())
                .milleage(car.getMilleage())
                .price(car.getPrice())
                .modelYear(car.getModelYear())
                .description(car.getDescription())
                .colour(car.getColour())
                .fuelType(car.getFuelType())
                .numDoors(car.getNumDoors())
                .build();
        carEntity = carRepository.save(carEntity);
        log.info("POST -> Coche añadido correctamente");
        return carConverter.toModel(carEntity);
    }

    @Override
    public Car getCarById(Integer id) {
        CarEntity carEntity = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Coche con ID " + id + " no encontrado."));
        log.info("GET -> Coche hallado correctamente");
        return carConverter.toModel(carEntity);
    }

    @Override
    public Car updateCar(Integer id, Car car) {
        CarEntity carEntity = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Coche con ID " + id + " no encontrado."));

        Optional<BrandEntity> brandOptional = brandRepository.findById(car.getBrand().getId());
        if (brandOptional.isEmpty()) {
            throw new BrandNotFoundException("Marca con ID " + car.getBrand().getId() + " no encontrada.");
        }
        BrandEntity brandEntity = brandOptional.get();
        carEntity.setBrand(brandEntity);
        carEntity.setModel(car.getModel());
        carEntity.setMilleage(car.getMilleage());
        carEntity.setPrice(car.getPrice());
        carEntity.setModelYear(car.getModelYear());
        carEntity.setDescription(car.getDescription());
        carEntity.setColour(car.getColour());
        carEntity.setFuelType(car.getFuelType());
        carEntity.setNumDoors(car.getNumDoors());

        carRepository.save(carEntity);
        log.info("PUT -> Coche actualizado correctamente");
        return carConverter.toModel(carEntity);
    }

    @Override
    public Car deleteById(Integer id) {
        CarEntity carEntity = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Coche con ID " + id + " no encontrado."));
        carRepository.deleteById(id);
        log.info("DELETE -> Coche eliminado correctamente");
        return carConverter.toModel(carEntity);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Car>> getAll() {
        log.info("GET -> Ejecutando en el hilo: {}", Thread.currentThread().getName());
        List<CarEntity> carEntityList = carRepository.findAll();
        List<Car> carsList = new ArrayList<>();
        carEntityList.forEach(car -> carsList.add(carConverter.toModel(car)));
        log.info("GET -> Lista encontrada correctamente");
        return CompletableFuture.completedFuture(carsList);
    }

    @Override
    public String carsCsv() {
        Pageable pageable = PageRequest.of(0, 100);
        List<CarEntity> carList = carRepository.findAll(pageable).getContent();
        StringBuilder csvContent = new StringBuilder();

        csvContent.append("Brand,Model,Colour,Description,ModelYear,Price,FuelType,NumDoors\n");

        for (CarEntity carEntity : carList) {
            String brandName = carEntity.getBrand() != null ? carEntity.getBrand().getName() : "Unknown";
            csvContent.append(brandName).append(",")
                    .append(carEntity.getModel()).append(",")
                    .append(carEntity.getColour()).append(",")
                    .append(carEntity.getDescription()).append(",")
                    .append(carEntity.getModelYear()).append(",")
                    .append(carEntity.getPrice()).append(",")
                    .append(carEntity.getFuelType()).append(",")
                    .append(carEntity.getNumDoors()).append("\n");
        }
        return csvContent.toString();
    }


    @Override
    public String uploadCars(MultipartFile file) {
        if (file.isEmpty()) {
            throw new CsvFileException("El archivo está vacío.");
        }

        if (!file.getOriginalFilename().contains(".csv")) {
            throw new CsvFileException("El archivo no es un CSV válido.");
        }

        List<CarEntity> carList = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader())) {

            // Itera sobre cada registro del archivo CSV
            for (CSVRecord record : csvParser) {
                CarEntity car = mapCarFromCsv(record); // Mapeo de los datos de cada coche
                carList.add(car);
            }

            // Guarda los coches en la base de datos
            carRepository.saveAll(carList);
            String msj = "Se han cargado y guardado " + carList.size() + " coches desde el archivo CSV.";
            log.info("POST -> {}", msj);
            return msj;

        } catch (IOException e) {
            throw new CsvFileException("No se pudo leer el archivo CSV");
        }
    }

    private CarEntity mapCarFromCsv(CSVRecord record) {
        CarEntity car = new CarEntity();
        // Verifica si la marca existe en la base de datos
        String brandName = record.get("brand");
        BrandEntity brand = brandRepository.findByName(brandName);
        if (brand == null) {
            throw new BrandNotFoundException("Marca no encontrada: " + brandName);
        }

        // Mapea los valores del CSV a la entidad CarEntity
        car.setBrand(brand);
        car.setModel(record.get("model"));
        car.setMilleage(Integer.parseInt(record.get("milleage")));
        car.setPrice(Double.parseDouble(record.get("price")));
        car.setModelYear(Integer.parseInt(record.get("modelYear")));
        car.setDescription(record.get("description"));
        car.setColour(record.get("colour"));
        car.setFuelType(record.get("fuelType"));
        car.setNumDoors(Integer.parseInt(record.get("numDoors")));

        return car;
    }
}
