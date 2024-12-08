package com.name.vehicleregistration.controller;

import com.name.vehicleregistration.controller.mappers.CarMapper;
import com.name.vehicleregistration.controller.dtos.CarRequest;
import com.name.vehicleregistration.controller.dtos.CarResponse;
import com.name.vehicleregistration.model.Car;
import com.name.vehicleregistration.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    final CarService carService;
    final CarMapper carMapper;

    @Operation(summary = "Call to add vehicles.")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<CarResponse> addCar(@RequestBody CarRequest carRequest) {
        Car car = carService.addCar(carRequest);
        CarResponse carResponse = carMapper.toResponse(car);
        return ResponseEntity.ok(carResponse);
    }

    @Operation (summary = "Call for car list.")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @GetMapping("/get")
    public CompletableFuture<?> carList() throws Exception {
        CompletableFuture<List<Car>> carList = carService.getAll();
        List<CarResponse> carResponsesList = new ArrayList<>();
        carList.get().forEach(car -> carResponsesList.add(carMapper.toResponse(car)));
        return CompletableFuture.completedFuture(carResponsesList);
    }

    @Operation (summary = "Call to obtain car by ID.")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @GetMapping("/get/{id}")
    public ResponseEntity<CarResponse> getCar(@PathVariable Integer id) {
        CarResponse carResponse = carMapper.toResponse(carService.getCarById(id));
        return ResponseEntity.ok(carResponse);
    }

    @Operation (summary = "Call to update car by ID.")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/put/{id}")
    public ResponseEntity<CarResponse> putCar (@PathVariable Integer id, @RequestBody CarRequest carRequest){
        CarResponse carResponse = carMapper.toResponse(carService.updateCar(id, carRequest));
        return ResponseEntity.ok(carResponse);
    }

    @Operation (summary = "Call to delete car by ID.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CarResponse> deleteCar (@PathVariable Integer id){
        CarResponse carResponse = carMapper.toResponse(carService.deleteById(id));
        return ResponseEntity.ok(carResponse);
    }

    @Operation (summary = "Call to download car list.")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @GetMapping("/downloadCars")
    public ResponseEntity<?> downloadCars(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "users.csv");

        byte[] csvBytes = carService.carsCsv().getBytes();
        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

    @Operation(summary = "Call to load a list of cars from a CSV file.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/uploadCsv")
    public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) {
        String result = carService.uploadCars(file);
        return ResponseEntity.ok(result);
    }

}
