package com.name.vehicleregistration.service.converters;

import com.name.vehicleregistration.entity.BrandEntity;
import com.name.vehicleregistration.model.Brand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class BrandConverterTest {
    @InjectMocks
    private BrandConverter brandConverter;

    @Test
    void toModel_test() {
        // Given
        BrandEntity brandEntity = BrandEntity.builder()
                .id(1)
                .name("Brand")
                .country("Country")
                .build();

        // When
        Brand brand = brandConverter.toModel(brandEntity);

        // Then
        assertEquals(brandEntity.getId(), brand.getId()) ;
        assertEquals(brandEntity.getName(), brand.getName());
        assertEquals(brandEntity.getCountry(), brand.getCountry());
    }

    @Test
    void toEntity_test() {
        // Given
        Brand brand = Brand.builder()
                .name("Brand")
                .country("Country")
                .build();

        // When
        BrandEntity brandEntity = brandConverter.toEntity(brand);

        // Then
        assertEquals(brand.getName(), brandEntity.getName());
        assertEquals(brand.getCountry(), brandEntity.getCountry());
        assertNull(brandEntity.getId());
    }
}

