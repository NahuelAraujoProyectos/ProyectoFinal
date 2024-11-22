package com.name.vehicleregistration.controller.mappers;

import com.name.vehicleregistration.controller.dtos.BrandRequest;
import com.name.vehicleregistration.controller.dtos.BrandResponse;
import com.name.vehicleregistration.model.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public Brand toModel(BrandRequest brandRequest) {
        return Brand.builder()
                .name(brandRequest.getName())
                .country(brandRequest.getCountry())
                .build();
    }

    public BrandResponse toResponse(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .country(brand.getCountry())
                .build();
    }
}
