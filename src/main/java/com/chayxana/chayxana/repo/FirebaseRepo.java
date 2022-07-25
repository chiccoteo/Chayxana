package com.chayxana.chayxana.repo;

import com.chayxana.chayxana.entity.Firebase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FirebaseRepo extends JpaRepository<Firebase, Long> {

    List<Firebase> findAllByUserIdAndRead(UUID user_id, boolean read);

    List<Firebase> findAllByUserId(UUID user_id);
}