package com.cbulims.cbulims.repository;

import com.cbulims.cbulims.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findTop2ByOrderByIdDesc();
}
