package com.name.vehicleregistration.service.impl;

import com.name.vehicleregistration.entity.BrandEntity;
import com.name.vehicleregistration.exception.brand.BrandAlreadyExistsException;
import com.name.vehicleregistration.exception.brand.BrandNotFoundException;
import com.name.vehicleregistration.model.Brand;
import com.name.vehicleregistration.repository.BrandRepository;
import com.name.vehicleregistration.service.BrandService;
import com.name.vehicleregistration.service.converters.BrandConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandConverter brandConverter;

    @Override
    public Brand addBrand(Brand brand) {
        BrandEntity brandEntity = brandConverter.toEntity(brand);
        if (brandRepository.existsByName(brandEntity.getName())) {
            throw new BrandAlreadyExistsException("Mark with name '" + brandEntity.getName() + "' already exists.");
        }
        brandRepository.save(brandEntity);
        log.info("POST -> Brand added successfully");
        return brandConverter.toModel(brandEntity);
    }

    @Override
    public Brand getBrandById(Integer id) {
        BrandEntity brandEntity = brandRepository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException("Brand with ID " + id + " not found."));
        log.info("GET -> Brand found correctly");
        return brandConverter.toModel(brandEntity);
    }

    @Override
    public Brand putBrand(Integer id, Brand brand) {
        BrandEntity brandEntity = brandRepository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException("Brand with ID " + id + " not found."));
        brandEntity.setName(brand.getName());
        brandEntity.setCountry(brand.getCountry());
        brandRepository.save(brandEntity);
        log.info("PUT -> Brand updated successfully");
        return brandConverter.toModel(brandEntity);
    }

    @Override
    public Brand deleteBrand(Integer id) {
        BrandEntity brandEntity = brandRepository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException("Brand with ID " + id + " not found."));
        brandRepository.deleteById(id);
        log.info("DELETE -> Brand removed successfully");
        return brandConverter.toModel(brandEntity);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Brand>> getAllBrands() {
        log.info("GET -> Running in thread: {}", Thread.currentThread().getName());
        List<BrandEntity> listBrandEntities = brandRepository.findAll();
        List<Brand> listBrand = new ArrayList<>();
        for (BrandEntity brandEntity : listBrandEntities) {
            listBrand.add(brandConverter.toModel(brandEntity));
        }
        log.info("GET -> List found successfully");
        return CompletableFuture.completedFuture(listBrand);
    }
}
