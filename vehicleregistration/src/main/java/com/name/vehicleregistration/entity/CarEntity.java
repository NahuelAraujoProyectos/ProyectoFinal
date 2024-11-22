package com.name.vehicleregistration.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "car")
public class CarEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandEntity brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer milleage;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer modelYear;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String colour;

    @Column(nullable = false)
    private String fuelType;

    @Column(nullable = false)
    private Integer numDoors;
}
