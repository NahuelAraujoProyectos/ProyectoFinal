package com.name.vehicleregistration.exception.custom.brand;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InvalidBrandDataException extends RuntimeException {
    public InvalidBrandDataException(String message) {
        super(message);
    }
}