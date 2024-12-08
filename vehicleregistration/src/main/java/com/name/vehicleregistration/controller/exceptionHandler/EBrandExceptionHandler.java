package com.name.vehicleregistration.controller.exceptionHandler;

import com.name.vehicleregistration.exception.brand.BrandAlreadyExistsException;
import com.name.vehicleregistration.exception.brand.BrandNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class EBrandExceptionHandler {

    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBrandNotFoundException(BrandNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        log.error("Error - brand not found: {}", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BrandAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleBrandAlreadyExistsException(BrandAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
        log.error("Error - email already exists: {}", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}