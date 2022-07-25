package com.chayxana.chayxana.repo;

import com.chayxana.chayxana.entity.Room;
import com.chayxana.chayxana.entity.RoomDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoomDetailRepo extends JpaRepository<RoomDetail, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
