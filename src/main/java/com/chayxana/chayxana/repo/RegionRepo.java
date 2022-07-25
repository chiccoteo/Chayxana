package com.chayxana.chayxana.repo;

import com.chayxana.chayxana.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepo extends JpaRepository<Region, Long> {

    boolean existsByName(String name);

    Region  getByName(String name);

    Optional<Region> findByName(String name);

}
