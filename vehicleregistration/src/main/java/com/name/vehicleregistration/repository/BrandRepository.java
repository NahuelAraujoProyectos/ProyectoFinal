package com.name.vehicleregistration.repository;

import com.name.vehicleregistration.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity,Integer> {
    boolean existsByName(String name);
    BrandEntity findByName(String name);
}
