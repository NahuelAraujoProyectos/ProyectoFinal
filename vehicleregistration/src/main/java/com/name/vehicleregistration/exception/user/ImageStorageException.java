package com.name.vehicleregistration.exception.user;

import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageStorageException extends RuntimeException {
    public ImageStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
