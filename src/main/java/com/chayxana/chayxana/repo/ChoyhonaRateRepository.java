package com.chayxana.chayxana.repo;

import com.chayxana.chayxana.entity.Chayxana;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChoyhonaRateRepository extends JpaRepository<Chayxana, UUID> {
}
