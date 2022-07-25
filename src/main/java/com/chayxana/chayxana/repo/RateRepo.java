package com.chayxana.chayxana.repo;

import com.chayxana.chayxana.entity.Rate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RateRepo extends JpaRepository<Rate, UUID> {
    List<Rate> findAllByChayxana_Id(UUID id);
//    List<Rate> findAllByChayxana_Id(UUID id, Pageable descOrAscByCreatedPageable);

//    Page<Rate> findAllByChayxana_IdOrderByCreatedAtAsc(UUID chayxana_id, Pageable pageable);

    Page<Rate> findAllByChayxanaIdOrderByCreatedAt(UUID chayxana_id, Pageable pageable);

}