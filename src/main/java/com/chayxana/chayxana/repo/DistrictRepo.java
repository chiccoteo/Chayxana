package com.chayxana.chayxana.repo;

import com.chayxana.chayxana.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DistrictRepo extends JpaRepository<District, Long> {

    boolean existsByNameAndRegion_Id(String name, Long region_id);

    List<District> findAllByRegion_Id(Long region_id);

    boolean existsByName(String string);

    District getByName(String name);

    Optional<District> findByName(String name);
}
