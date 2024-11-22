package com.name.vehicleregistration.service;

import com.name.vehicleregistration.controller.dtos.CarRequest;
import com.name.vehicleregistration.model.Car;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface CarService {
    Car addCar(CarRequest carRequest);
    Car getCarById(Integer id);
    Car updateCar(Integer id, CarRequest carRequest);
    Car deleteById(Integer id);
    CompletableFuture<List<Car>> getAll();
    String carsCsv();
    String uploadCars(MultipartFile file);
}
