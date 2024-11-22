package com.name.vehicleregistration.model;

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String fullName;
    private String email;
    private String role;
    private String createdAt;
    private String updatedAt;

    private boolean enabled;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;
}
