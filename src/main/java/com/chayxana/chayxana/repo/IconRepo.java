package com.chayxana.chayxana.repo;

import com.chayxana.chayxana.entity.Icon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IconRepo extends JpaRepository<Icon, UUID> {


}
