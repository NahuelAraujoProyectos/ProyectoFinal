package com.name.vehicleregistration.service;

import com.name.vehicleregistration.model.Brand;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface BrandService {
    Brand addBrand(Brand brand);
    Brand getBrandById(Integer id);
    Brand putBrand(Integer id, Brand brand);
    Brand deleteBrand(Integer id);
    CompletableFuture<List<Brand>> getAllBrands();
}
