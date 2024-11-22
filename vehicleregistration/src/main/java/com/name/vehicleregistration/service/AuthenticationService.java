package com.name.vehicleregistration.service;

import com.name.vehicleregistration.controller.dtos.LoginRequest;
import com.name.vehicleregistration.controller.dtos.LoginResponse;
import com.name.vehicleregistration.controller.dtos.SignUpRequest;

public interface AuthenticationService {
    LoginResponse signup(SignUpRequest signUpRequest);
    LoginResponse login (LoginRequest loginRequest);
}
