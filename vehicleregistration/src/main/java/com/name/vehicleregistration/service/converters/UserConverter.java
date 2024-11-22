package com.name.vehicleregistration.service.converters;

import com.name.vehicleregistration.entity.UserEntity;
import com.name.vehicleregistration.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserConverter {

    public User toModel (UserEntity userEntity){
        return new User(
                userEntity.getId(),
                userEntity.getFullName(),
                userEntity.getEmail(),
                userEntity.getRole(),
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt(),
                userEntity.isAccountNonExpired(),
                userEntity.isAccountNonLocked(),
                userEntity.isCredentialsNonExpired(),
                userEntity.isEnabled()
        );
    }
}