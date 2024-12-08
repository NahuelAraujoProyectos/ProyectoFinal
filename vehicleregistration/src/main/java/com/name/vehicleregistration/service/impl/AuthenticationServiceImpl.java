package com.name.vehicleregistration.service.impl;

import com.name.vehicleregistration.controller.dtos.LoginRequest;
import com.name.vehicleregistration.controller.dtos.LoginResponse;
import com.name.vehicleregistration.controller.dtos.SignUpRequest;
import com.name.vehicleregistration.entity.UserEntity;
import com.name.vehicleregistration.exception.authentication.EmailAlreadyInUseException;
import com.name.vehicleregistration.repository.UserRepository;
import com.name.vehicleregistration.service.AuthenticationService;
import com.name.vehicleregistration.exception.authentication.InvalidCredentialsException;
import com.name.vehicleregistration.exception.authentication.UserNotFoundException;
import com.name.vehicleregistration.service.utils.RoleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleUtils roleUtils;

    public LoginResponse signup(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new EmailAlreadyInUseException("Email " + signUpRequest.getEmail() + " is already in use.");
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
            var user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("Incorrect email."));

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new InvalidCredentialsException("Incorrect password.");
            }

            var jwt = JwtService.generateToken(user);
            return LoginResponse.builder().token(jwt).build();
        } catch (UserNotFoundException | InvalidCredentialsException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidCredentialsException("Error trying to log in.");
        }
    }
}