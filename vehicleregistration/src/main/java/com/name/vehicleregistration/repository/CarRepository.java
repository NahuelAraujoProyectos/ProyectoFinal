package com.name.vehicleregistration.repository;

import com.name.vehicleregistration.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<CarEntity,Integer> {
}
