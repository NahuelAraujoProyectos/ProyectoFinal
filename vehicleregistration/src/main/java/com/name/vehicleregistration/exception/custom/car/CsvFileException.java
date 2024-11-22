package com.name.vehicleregistration.exception.custom.car;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CsvFileException extends RuntimeException {
    public CsvFileException(String message) {
        super(message);
    }
}
