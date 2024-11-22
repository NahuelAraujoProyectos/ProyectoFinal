package com.name.vehicleregistration.controller.mapper;

import com.name.vehicleregistration.controller.dtos.UserRequest;
import com.name.vehicleregistration.controller.dtos.UserResponse;
import com.name.vehicleregistration.controller.mappers.UserMapper;
import com.name.vehicleregistration.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {
    @InjectMocks
    private UserMapper userMapper;

    @Test
    void toResponse_test(){
        // Given
        User user = User.builder()
                .id(1)
                .fullName("FullName")
                .email("email@email.com")
                .role("ROLE")
                .createdAt((new Date()).toString())
                .updatedAt((new Date()).toString())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        // When
        UserResponse userResponse = userMapper.toResponse(user);

        // Then
        assertEquals(userResponse.getId(), user.getId());
        assertEquals(userResponse.getFullName(), user.getFullName());
        assertEquals(userResponse.getEmail(), user.getEmail());
        assertEquals(userResponse.getRole(), user.getRole());
        assertEquals(userResponse.isAccountNonExpired(), user.isAccountNonExpired());
        assertEquals(userResponse.isAccountNonLocked(), user.isAccountNonLocked());
        assertEquals(userResponse.isCredentialsNonExpired(), user.isCredentialsNonExpired());
        assertEquals(userResponse.isEnabled(), user.isEnabled());
    }


    @Test
    void toModel_test(){
        // Given
        UserRequest userRequest = UserRequest.builder()
                .fullName("FullName")
                .email("email@email.com")
                .role("ROLE")
                .build();

        // When
        User user = userMapper.toModel(userRequest);

        // Then
        assertEquals(userRequest.getFullName(), user.getFullName());
        assertEquals(userRequest.getEmail(), user.getEmail());
        assertEquals(userRequest.getRole(), user.getRole());
    }

}
