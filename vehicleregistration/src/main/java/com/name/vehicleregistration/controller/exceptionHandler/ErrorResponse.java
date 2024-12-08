package com.name.vehicleregistration.controller.exceptionHandler;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponse {
    private String message;
    private HttpStatus status;
    private LocalDateTime timestamp;
}