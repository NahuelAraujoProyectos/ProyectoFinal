package com.name.vehicleregistration.exception.custom.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InvalidImageFormatException extends RuntimeException {
    public InvalidImageFormatException(String message) {
        super(message);
    }
}