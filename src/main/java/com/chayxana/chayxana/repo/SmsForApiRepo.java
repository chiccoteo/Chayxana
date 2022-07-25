package com.chayxana.chayxana.repo;

import com.chayxana.chayxana.entity.SmsForToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SmsForApiRepo extends JpaRepository<SmsForToken, Long> {
    @Query(value = "SELECT max(id) FROM SmsForToken")
    Long maxId();
}
