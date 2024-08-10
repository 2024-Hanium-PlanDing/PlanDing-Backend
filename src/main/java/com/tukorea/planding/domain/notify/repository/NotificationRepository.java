package com.tukorea.planding.domain.notify.repository;

import com.tukorea.planding.domain.notify.entity.Notification;
import com.tukorea.planding.domain.notify.entity.NotificationType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByUserCodeAndNotificationTypeAndMessageAndGroupNameAndUrl(String userCode, NotificationType notificationType, String message, String groupName, String url);

    @Query("SELECT n FROM Notification n WHERE n.userCode = :userCode")
    List<Notification> findByUserCode(@Param("userCode") String userCode);

}
