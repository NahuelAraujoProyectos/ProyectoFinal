package com.name.vehicleregistration.exception.custom.car;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DataMappingException extends RuntimeException {
    public DataMappingException(String message) {
        super(message);
    }
}
