package com.name.vehicleregistration.service.impl;

import com.name.vehicleregistration.controller.dtos.LoginRequest;
import com.name.vehicleregistration.controller.dtos.LoginResponse;
import com.name.vehicleregistration.controller.dtos.SignUpRequest;
import com.name.vehicleregistration.entity.UserEntity;
import com.name.vehicleregistration.exception.custom.authentication.EmailAlreadyInUseException;
import com.name.vehicleregistration.repository.UserRepository;
import com.name.vehicleregistration.service.AuthenticationService;
import com.name.vehicleregistration.exception.custom.authentication.InvalidCredentialsException;
import com.name.vehicleregistration.exception.custom.authentication.UserNotFoundException;
import com.name.vehicleregistration.utils.RoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final RoleUtils roleUtils;

    public LoginResponse signup(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new EmailAlreadyInUseException("Email " + signUpRequest.getEmail() + " ya está en uso.");
        }

        UserEntity userEntity = UserEntity
                .builder()
                .fullName(signUpRequest.getFullName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(roleUtils.testRole(signUpRequest.getRole()))
                .build();

        userEntity = userRepository.save(userEntity);

        var jwt = JwtService.generateToken(userEntity);
        return LoginResponse.builder().token(jwt).build();
    }

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            // Intentar autenticar al usuario sin usar excepciones de Spring Security
            var user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("Email incorrecto."));

            // Verificar la contraseña manualmente
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new InvalidCredentialsException("Contraseña incorrecta.");
            }

            var jwt = JwtService.generateToken(user);
            return LoginResponse.builder().token(jwt).build();
        } catch (UserNotFoundException | InvalidCredentialsException e) {
            throw e; // Re-lanzar la excepción personalizada
        } catch (Exception e) {
            throw new InvalidCredentialsException("Error al intentar iniciar sesión.");
        }
    }
}