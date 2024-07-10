package com.tukorea.planding.domain.notify.service;

import com.tukorea.planding.domain.notify.dto.NotificationDTO;
import com.tukorea.planding.domain.notify.dto.NotificationScheduleRequest;
import com.tukorea.planding.domain.notify.entity.NotificationType;
import com.tukorea.planding.domain.schedule.entity.PersonalSchedule;
import com.tukorea.planding.domain.schedule.repository.PersonalScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleNotificationScheduler {

    private final ScheduleNotificationService scheduleNotificationService;
    private final NotificationService notificationService;

    @Scheduled(fixedRate = 60000)  // 1분마다 실행
    @Transactional
    public void sendScheduleNotifications() {

        Set<ZSetOperations.TypedTuple<Object>> dueNotifications = scheduleNotificationService.getDueNotifications();

        for (ZSetOperations.TypedTuple<Object> tuple : dueNotifications) {
            NotificationDTO notification = (NotificationDTO) tuple.getValue();
            notificationService.sendPersonalNotification(notification.getUserCode(), notification);
            scheduleNotificationService.removeNotification(notification);
        }
    }

}
