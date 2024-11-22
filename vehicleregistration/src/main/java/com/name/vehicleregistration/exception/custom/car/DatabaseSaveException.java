package com.name.vehicleregistration.exception.custom.car;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DatabaseSaveException extends RuntimeException {
    public DatabaseSaveException(String message) {
        super(message);
    }
}
