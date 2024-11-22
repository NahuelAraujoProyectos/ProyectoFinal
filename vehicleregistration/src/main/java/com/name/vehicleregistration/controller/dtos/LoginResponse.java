package com.name.vehicleregistration.controller.dtos;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginResponse {
    private String token;
}
