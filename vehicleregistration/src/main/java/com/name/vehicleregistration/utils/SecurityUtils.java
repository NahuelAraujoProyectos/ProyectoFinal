package com.name.vehicleregistration.utils;

import com.name.vehicleregistration.entity.ProfileEntity;
import com.name.vehicleregistration.entity.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    // Método para obtener el usuario autenticado desde el contexto de seguridad
    public static UserEntity getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            // Si el principal es una instancia de UserDetails, lo casteamos y obtenemos la información deseada
            return (UserEntity) principal;
        } else {
            return null;
        }
    }
}