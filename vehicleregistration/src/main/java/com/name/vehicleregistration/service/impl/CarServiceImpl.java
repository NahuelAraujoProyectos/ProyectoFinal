package com.name.vehicleregistration.service.impl;

import com.name.vehicleregistration.controller.dtos.CarRequest;
import com.name.vehicleregistration.entity.BrandEntity;
import com.name.vehicleregistration.entity.CarEntity;
import com.name.vehicleregistration.exception.car.BrandNotFoundException;
import com.name.vehicleregistration.exception.car.CarNotFoundException;
import com.name.vehicleregistration.exception.car.CsvFileException;
import com.name.vehicleregistration.model.Car;
import com.name.vehicleregistration.repository.BrandRepository;
import com.name.vehicleregistration.repository.CarRepository;
import com.name.vehicleregistration.service.CarService;
import com.name.vehicleregistration.service.converters.CarConverter;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarConverter carConverter;
    private final BrandRepository brandRepository;

    @Override
    public Car addCar(CarRequest carRequest) {
        BrandEntity brandEntity = brandRepository.findById(carRequest.getBrandId())
                .orElseThrow(() -> new BrandNotFoundException("Brand with ID " + carRequest.getBrandId() + " not found."));

        CarEntity carEntity = CarEntity.builder()
                .brand(brandEntity)
                .model(carRequest.getModel())
                .milleage(carRequest.getMilleage())
                .price(carRequest.getPrice())
                .modelYear(carRequest.getModelYear())
                .description(carRequest.getDescription())
                .colour(carRequest.getColour())
                .fuelType(carRequest.getFuelType())
                .numDoors(carRequest.getNumDoors())
                .build();
        carEntity = carRepository.save(carEntity);
        log.info("POST -> Car added successfully");
        return carConverter.toModel(carEntity);
    }

    @Override
    public Car getCarById(Integer id) {
        CarEntity carEntity = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car with ID " + id + " not found."));
        log.info("GET -> Car found correctly");
        return carConverter.toModel(carEntity);
    }

    @Override
    public Car updateCar(Integer id, CarRequest carRequest) {
        CarEntity carEntity = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car with ID " + id + " not found."));

        Optional<BrandEntity> brandOptional = brandRepository.findById(carRequest.getBrandId());
        if (brandOptional.isEmpty()) {
            throw new BrandNotFoundException("Brand with ID " + carRequest.getBrandId() + " not found.");
        }
        BrandEntity brandEntity = brandOptional.get();
        carEntity.setBrand(brandEntity);
        carEntity.setModel(carRequest.getModel());
        carEntity.setMilleage(carRequest.getMilleage());
        carEntity.setPrice(carRequest.getPrice());
        carEntity.setModelYear(carRequest.getModelYear());
        carEntity.setDescription(carRequest.getDescription());
        carEntity.setColour(carRequest.getColour());
        carEntity.setFuelType(carRequest.getFuelType());
        carEntity.setNumDoors(carRequest.getNumDoors());

        carRepository.save(carEntity);
        log.info("PUT -> Successfully updated car");
        return carConverter.toModel(carEntity);
    }

    @Override
    public Car deleteById(Integer id) {
        CarEntity carEntity = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car with ID " + id + " not found."));
        carRepository.deleteById(id);
        log.info("DELETE -> Car removed successfully");
        return carConverter.toModel(carEntity);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Car>> getAll() {
        log.info("GET -> Running in thread: {}", Thread.currentThread().getName());
        List<CarEntity> carEntityList = carRepository.findAll();
        List<Car> carsList = new ArrayList<>();
        carEntityList.forEach(car -> carsList.add(carConverter.toModel(car)));
        log.info("GET -> List found successfully");
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
            throw new CsvFileException("The file is empty.");
        }

        if (!file.getOriginalFilename().contains(".csv")) {
            throw new CsvFileException("The file is not a valid CSV.");
        }

        List<CarEntity> carList = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord record : csvParser) {
                CarEntity car = new CarEntity();

                String brandName = record.get("brand");
                BrandEntity brand = brandRepository.findByName(brandName);
                if (brand == null) {
                    throw new BrandNotFoundException("Brand not found: " + brandName);
                }

                car.setBrand(brand);
                car.setModel(record.get("model"));
                car.setMilleage(Integer.parseInt(record.get("milleage")));
                car.setPrice(Double.parseDouble(record.get("price")));
                car.setModelYear(Integer.parseInt(record.get("modelYear")));
                car.setDescription(record.get("description"));
                car.setColour(record.get("colour"));
                car.setFuelType(record.get("fuelType"));
                car.setNumDoors(Integer.parseInt(record.get("numDoors")));

                carList.add(car);
            }

            carRepository.saveAll(carList);
            String msj =  carList.size() + " cars have been uploaded and saved from the CSV file.";
            log.info("POST -> {}", msj);
            return msj;

        } catch (IOException e) {
            throw new CsvFileException("Could not read CSV file");
        }
    }

}
