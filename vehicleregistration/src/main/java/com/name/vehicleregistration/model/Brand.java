package com.name.vehicleregistration.model;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
    private Integer id;
    private String name;
    private String country;
}
