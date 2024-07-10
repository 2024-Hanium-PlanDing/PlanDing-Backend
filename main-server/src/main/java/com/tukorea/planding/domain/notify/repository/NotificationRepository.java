package com.tukorea.planding.domain.notify.repository;

import com.tukorea.planding.domain.notify.entity.Notification;
import com.tukorea.planding.domain.notify.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByUserCodeAndNotificationTypeAndMessageAndGroupNameAndUrl(String userCode, NotificationType notificationType, String message, String groupName, String url);
}
