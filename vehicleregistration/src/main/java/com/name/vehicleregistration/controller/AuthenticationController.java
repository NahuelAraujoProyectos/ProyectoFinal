package com.name.vehicleregistration.controller;

import com.name.vehicleregistration.controller.dtos.LoginRequest;
import com.name.vehicleregistration.controller.dtos.LoginResponse;
import com.name.vehicleregistration.controller.dtos.SignUpRequest;
import com.name.vehicleregistration.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation (summary = "Llamada para registrar usuario")
    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody SignUpRequest signUpRequest){
        LoginResponse token = authenticationService.signup(signUpRequest);
        log.info("POST -> Perfil registrado correctamente.");
        return ResponseEntity.ok(token);
    }

    @Operation (summary = "Llamada para logar usuario")
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest) {
        log.info("POST -> Perfil encontrado correctamente.");
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }
}
