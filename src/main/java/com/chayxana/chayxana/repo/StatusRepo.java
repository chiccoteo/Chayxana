package com.chayxana.chayxana.repo;

import com.chayxana.chayxana.entity.Status;
import com.chayxana.chayxana.entity.enums.StatusName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepo extends JpaRepository<Status, Long> {

    Optional<Status> findByStatusName(StatusName statusName);
}