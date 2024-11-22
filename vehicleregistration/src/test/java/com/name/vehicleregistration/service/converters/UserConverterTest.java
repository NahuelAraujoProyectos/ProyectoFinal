package com.name.vehicleregistration.service.converters;

import com.name.vehicleregistration.entity.UserEntity;
import com.name.vehicleregistration.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserConverterTest {

    @InjectMocks
    private UserConverter userConverter;

    @Test
    public void testToModel() {
        // Given
        UserEntity userEntity = UserEntity.builder()
                .id(1)
                .fullName("FullName")
                .email("name@example.com")
                .role("ADMIN")
                .createdAt("2024-11-18")
                .updatedAt("2024-11-18")
                .build();

        // When:
        User user = userConverter.toModel(userEntity);

        // Then:
        assertEquals(userEntity.getId(), user.getId());
        assertEquals(userEntity.getFullName(), user.getFullName());
        assertEquals(userEntity.getEmail(), user.getEmail());
        assertEquals(userEntity.getRole(), user.getRole());
        assertEquals(userEntity.getCreatedAt(), user.getCreatedAt());
        assertEquals(userEntity.getUpdatedAt(), user.getUpdatedAt());
    }
}
