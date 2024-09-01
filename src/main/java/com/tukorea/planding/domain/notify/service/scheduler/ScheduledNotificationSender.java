package com.tukorea.planding.domain.notify.service.scheduler;

import com.tukorea.planding.domain.notify.dto.alarm.NotificationDTO;
import com.tukorea.planding.domain.notify.service.ScheduleNotificationService;
import com.tukorea.planding.domain.notify.service.schedule.NotificationHandler;
import com.tukorea.planding.domain.notify.service.schedule.NotificationHandlerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;

@Component
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ScheduledNotificationSender {

    private final ScheduleNotificationService scheduleNotificationService;
    private final NotificationHandlerFactory notificationHandlerFactory;

    @Scheduled(fixedRate = 60000)  // 1분마다 실행
    public void sendScheduleNotifications() {
        LocalDateTime now = LocalDateTime.now();
        Set<ZSetOperations.TypedTuple<Object>> dueNotifications = scheduleNotificationService.getDueNotifications();

        for (ZSetOperations.TypedTuple<Object> tuple : dueNotifications) {
            NotificationDTO notification = (NotificationDTO) tuple.getValue();

            Number scoreNumber = tuple.getScore();
            long score = scoreNumber.longValue();
            LocalDateTime decodedDateTime = LocalDateTime.ofEpochSecond(score, 0, ZoneOffset.UTC);
            // 1시간 전 로직
            if (isNotificationDue(now, decodedDateTime)) {
                try {
                    NotificationHandler handler = notificationHandlerFactory.getHandler(notification.getNotificationType());
                    handler.sendNotification(notification);
                    scheduleNotificationService.removeNotification(notification);
                } catch (Exception e) {
                    log.error("알림 전송 중 오류 발생: {}", e.getMessage(), e);
                }
            }
        }
    }


    private boolean isNotificationDue(LocalDateTime now, LocalDateTime notificationTime) {
        return notificationTime.toLocalDate().equals(now.toLocalDate()) && notificationTime.getHour() == now.getHour();
    }


}
