package com.chayxana.chayxana.repo;

import com.chayxana.chayxana.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo extends JpaRepository<Notification, Long> {
}
