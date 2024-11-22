package com.name.vehicleregistration.controller.mapper;

import com.name.vehicleregistration.controller.dtos.BrandRequest;
import com.name.vehicleregistration.controller.dtos.BrandResponse;
import com.name.vehicleregistration.controller.mappers.BrandMapper;
import com.name.vehicleregistration.model.Brand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BrandMapperTest {

    @InjectMocks
    private BrandMapper brandMapper;

    @Test
    void toModel_test() {
        // Given
        BrandRequest brandRequest = new BrandRequest();
        brandRequest.setName("Brand");
        brandRequest.setCountry("Country");

        // When
        Brand brand = brandMapper.toModel(brandRequest);

        // Then
        assertEquals(brandRequest.getName(), brand.getName());
        assertEquals(brandRequest.getCountry(), brand.getCountry());
    }

    @Test
    void toResponse_test() {
        // Given
        Brand brand = new Brand();
        brand.setId(1);
        brand.setName("Brand");
        brand.setCountry("Country");

        // When
        BrandResponse brandResponse = brandMapper.toResponse(brand);

        // Then
        assertEquals(brand.getId(), brandResponse.getId()) ;
        assertEquals(brand.getName(), brandResponse.getName());
        assertEquals(brand.getCountry(), brandResponse.getCountry());
    }

}