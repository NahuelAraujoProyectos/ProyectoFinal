package com.name.vehicleregistration.service.utils;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RoleUtils {
    public String testRole(String role){
        String finalRole = "ROLE_" + role;
        Set<String> validRoles = Set.of("ROLE_ADMIN", "ROLE_CLIENT");
        if (!validRoles.contains(finalRole)) {
            finalRole = "ROLE_CLIENT";
        }
        return finalRole;
    }
}
