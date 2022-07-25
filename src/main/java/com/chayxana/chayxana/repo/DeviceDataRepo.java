package com.chayxana.chayxana.repo;

import com.chayxana.chayxana.entity.DeviceData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceDataRepo extends JpaRepository<DeviceData, UUID> {

    Optional<DeviceData> findByDeviceId(String deviceId);

    List<DeviceData> findAllByUserIdAndActiveTrue(UUID user_id);

    Optional<DeviceData> findByUserId(UUID user_id);

    List<DeviceData> findAllByActiveTrue();
}
