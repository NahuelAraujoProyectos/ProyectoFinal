package com.name.vehicleregistration.service.impl;

import com.name.vehicleregistration.entity.BrandEntity;
import com.name.vehicleregistration.exception.custom.brand.BrandAlreadyExistsException;
import com.name.vehicleregistration.exception.custom.brand.BrandNotFoundException;
import com.name.vehicleregistration.model.Brand;
import com.name.vehicleregistration.repository.BrandRepository;
import com.name.vehicleregistration.service.BrandService;
import com.name.vehicleregistration.service.converters.BrandConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private BrandConverter brandConverter;

    @Override
    public Brand addBrand(Brand brand) {
        BrandEntity brandEntity = brandConverter.toEntity(brand);
        if (brandRepository.existsByName(brandEntity.getName())) {
            throw new BrandAlreadyExistsException("La marca con nombre '" + brandEntity.getName() + "' ya existe.");
        }
        brandRepository.save(brandEntity);
        log.info("POST -> Marca añadida correctamente");
        return brandConverter.toModel(brandEntity);
    }

    @Override
    public Brand getBrandById(Integer id) {
        BrandEntity brandEntity = brandRepository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException("Marca con ID " + id + " no encontrada."));
        log.info("GET -> Marca encontrada correctamente");
        return brandConverter.toModel(brandEntity);
    }

    @Override
    public Brand putBrand(Integer id, Brand brand) {
        BrandEntity brandEntity = brandRepository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException("Marca con ID " + id + " no encontrada."));
        brandEntity.setName(brand.getName());
        brandEntity.setCountry(brand.getCountry());
        brandRepository.save(brandEntity);
        log.info("PUT -> Marca actualizada correctamente");
        return brandConverter.toModel(brandEntity);
    }

    @Override
    public Brand deleteBrand(Integer id) {
        BrandEntity brandEntity = brandRepository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException("Marca con ID " + id + " no encontrada."));
        brandRepository.deleteById(id);
        log.info("DELETE -> Marca eliminada correctamente");
        return brandConverter.toModel(brandEntity);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<Brand>> getAllBrands() {
        log.info("GET -> Ejecutando en el hilo: {}", Thread.currentThread().getName());
        List<BrandEntity> listBrandEntities = brandRepository.findAll();
        List<Brand> listBrand = new ArrayList<>();
        for (BrandEntity brandEntity : listBrandEntities) {
            listBrand.add(brandConverter.toModel(brandEntity));
        }
        log.info("GET -> Lista encontrada correctamente");
        return CompletableFuture.completedFuture(listBrand);
    }
}
