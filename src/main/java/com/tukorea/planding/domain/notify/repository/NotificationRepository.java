package com.tukorea.planding.domain.notify.repository;

import com.tukorea.planding.domain.notify.dto.NotificationScheduleResponse;
import com.tukorea.planding.domain.notify.entity.Notification;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT new com.tukorea.planding.domain.notify.dto.NotificationScheduleResponse(" +
            "n.id, n.message, n.groupName, n.url, n.createdDate) " +
            "FROM Notification n " +
            "WHERE n.userCode = :userCode")
    List<NotificationScheduleResponse> findByUserCode(@Param("userCode") String userCode);

}
