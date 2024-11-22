package com.name.vehicleregistration.controller;

import com.name.vehicleregistration.controller.dtos.BrandRequest;
import com.name.vehicleregistration.controller.dtos.BrandResponse;
import com.name.vehicleregistration.controller.mappers.BrandMapper;
import com.name.vehicleregistration.model.Brand;
import com.name.vehicleregistration.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping("/brands")
public class BrandController {

    private final BrandService brandService;
    private final BrandMapper brandMapper;

    public BrandController(BrandService brandService, BrandMapper brandMapper) {
        this.brandService = brandService;
        this.brandMapper = brandMapper;
    }

    @Operation(summary = "Añadir una nueva marca")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<BrandResponse> addBrand(@RequestBody BrandRequest brandRequest){
        Brand brand = brandService.addBrand(brandMapper.toModel(brandRequest));
        return ResponseEntity.ok(brandMapper.toResponse(brand));
    }

    @Operation(summary = "Obtener todas las marcas")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @GetMapping("/get")
    public CompletableFuture<List<BrandResponse>> getAllBrands() throws Exception {
        CompletableFuture<List<Brand>> list = brandService.getAllBrands();
        List<BrandResponse> listResponse = new ArrayList<>();
        list.get().forEach(brand -> listResponse.add(brandMapper.toResponse(brand)));
        return CompletableFuture.completedFuture(listResponse);
    }

    @Operation(summary = "Obtener marca por ID")
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    @GetMapping("/get/{id}")
    public ResponseEntity<BrandResponse> getBrand(@PathVariable Integer id){
        Brand brand = brandService.getBrandById(id);
        return ResponseEntity.ok(brandMapper.toResponse(brand));
    }

    @Operation(summary = "Editar una marca por ID")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/put/{id}")
    public ResponseEntity<BrandResponse> putBrand(@PathVariable Integer id, @RequestBody BrandRequest brandRequest){
        Brand brand = brandService.putBrand(id, brandMapper.toModel(brandRequest));
        return ResponseEntity.ok(brandMapper.toResponse(brand));
    }

    @Operation(summary = "Eliminar una marca por ID")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BrandResponse> deleteBrand(@PathVariable Integer id){
        Brand brand = brandService.deleteBrand(id); // Lógica para eliminar
        BrandResponse response = brandMapper.toResponse(brand); // Asegúrate de que response tiene el campo "name"
        return ResponseEntity.ok(response);
    }
}
