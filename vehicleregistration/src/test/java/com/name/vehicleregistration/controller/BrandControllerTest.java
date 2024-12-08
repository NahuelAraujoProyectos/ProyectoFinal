package com.name.vehicleregistration.controller;

import com.name.vehicleregistration.controller.dtos.BrandRequest;
import com.name.vehicleregistration.controller.dtos.BrandResponse;
import com.name.vehicleregistration.model.Brand;
import com.name.vehicleregistration.repository.BrandRepository;
import com.name.vehicleregistration.service.BrandService;
import com.name.vehicleregistration.controller.mappers.BrandMapper;
import com.name.vehicleregistration.service.UserService;
import com.name.vehicleregistration.service.converters.BrandConverter;
import com.name.vehicleregistration.service.impl.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BrandController.class)
class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandService brandService;

    @MockBean
    private BrandRepository brandRepository;

    @MockBean
    private BrandConverter brandConverter;

    @MockBean
    private BrandMapper brandMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserService profileService;

    @Test
    @WithMockUser(username = "admin", password = "mmm", roles = "ADMIN")
    void addBrand_test() throws Exception {
        // Given
        BrandRequest brandRequest = BrandRequest.builder()
                .name("BrandTest")
                .build();

        Brand brand = Brand.builder()
                .id(1)
                .name("BrandTest")
                .build();

        BrandResponse brandResponse = BrandResponse.builder()
                .id(1)
                .name("BrandTest")
                .build();

        // When
        when(brandMapper.toModel(brandRequest)).thenReturn(brand);
        when(brandService.addBrand(brand)).thenReturn(brand);
        when(brandMapper.toResponse(brand)).thenReturn(brandResponse);

        // Then
        mockMvc.perform(post("/brands/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"BrandTest\"}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("BrandTest")));
    }

    @Test
    @WithMockUser(username = "admin", password = "mmm", roles = "ADMIN")
    void getBrandById_test() throws Exception {
        // Crear un objeto Brand para simular la respuesta del servicio
        Brand brand = Brand.builder()
                .id(1)
                .name("BrandTest")
                .build();

        BrandResponse brandResponse = BrandResponse.builder()
                .id(1)
                .name("BrandTest")
                .build();

        // Configuración de mock para el servicio y mapper
        when(brandService.getBrandById(1)).thenReturn(brand);
        when(brandMapper.toResponse(brand)).thenReturn(brandResponse);

        // Realizar la solicitud GET
        mockMvc.perform(get("/brands/get/{id}", 1)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("BrandTest")));
    }

    @Test
    @WithMockUser(username = "admin", password = "mmm", roles = "ADMIN")
    void putBrand_test() throws Exception {
        // Given
        Brand brand = new Brand();
        brand.setId(1);
        brand.setName("Marca a Modificar");

        Brand newBrand = new Brand();
        newBrand.setId(1);
        newBrand.setName("Marca Modificada");

        BrandRequest brandRequest = new BrandRequest();
        brandRequest.setName("Marca a Modificar");

        BrandResponse brandResponse = new BrandResponse();
        brandResponse.setId(1);
        brandResponse.setName("Marca Modificada");

        // When
        when(brandService.putBrand(eq(1), eq(brand))).thenReturn(newBrand);
        when(brandMapper.toModel(eq(brandRequest))).thenReturn(brand);
        when(brandMapper.toResponse(newBrand)).thenReturn(brandResponse);

        // Then
        mockMvc.perform(put("/brands/put/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Marca a Modificar\"}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Marca Modificada")));
    }

    @Test
    @WithMockUser(username = "admin", password = "mmm", roles = "ADMIN")
    void deleteBrand_test() throws Exception {
        // Given
        Brand brand = Brand.builder()
                .id(1)
                .name("BrandTest")
                .build();

        BrandResponse brandResponse = BrandResponse.builder()
                .id(1)
                .name("BrandTest")
                .build();

        // When
        when(brandService.deleteBrand(1)).thenReturn(brand);
        when(brandMapper.toResponse(brand)).thenReturn(brandResponse);

        // Then
        mockMvc.perform(delete("/brands/delete/{id}", 1)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("BrandTest")));
    }

}
