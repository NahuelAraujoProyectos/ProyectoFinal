package com.name.vehicleregistration.service.converters;

import com.name.vehicleregistration.entity.BrandEntity;
import com.name.vehicleregistration.model.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandConverter {

    // Convierte la entidad BrandEntity al modelo Brand
    public Brand toModel(BrandEntity brandEntity) {
        return Brand.builder()
                .id(brandEntity.getId())
                .name(brandEntity.getName())
                .country(brandEntity.getCountry())
                .build();
    }

    // Convierte el modelo Brand a la entidad BrandEntity
    public BrandEntity toEntity(Brand brand) {
        return BrandEntity.builder()
                .name(brand.getName())
                .country(brand.getCountry())
                .build();
        }
}


