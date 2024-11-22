package com.name.vehicleregistration.controller.mappers;

import com.name.vehicleregistration.controller.dtos.UserRequest;
import com.name.vehicleregistration.controller.dtos.UserResponse;
import com.name.vehicleregistration.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserMapper {

    public UserResponse toResponse (User user){
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .enabled(user.isEnabled())
                .build();
    }

    public User toModel (UserRequest userRequest){
        return User.builder()
                .fullName(userRequest.getFullName())
                .email(userRequest.getEmail())
                .role(userRequest.getRole())
                .build();
    }
}
