package com.tukorea.planding.domain.notify.service;

import com.tukorea.planding.domain.notify.dto.alarm.NotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduleNotificationService {


    private static final String SCHEDULE_NOTIFICATION_KEY = "schedule_notifications";
    private final RedisTemplate<String, Object> redisTemplate;


    public void scheduleNotification(NotificationDTO notificationDTO, LocalDateTime notificationTime) {
        long score = notificationTime.toEpochSecond(ZoneOffset.UTC);
        redisTemplate.opsForZSet().add(SCHEDULE_NOTIFICATION_KEY, notificationDTO, score);
    }

    public Set<ZSetOperations.TypedTuple<Object>> getDueNotifications() {
        long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        return redisTemplate.opsForZSet().rangeByScoreWithScores(SCHEDULE_NOTIFICATION_KEY, 0, now);
    }

    public void removeNotification(NotificationDTO notificationDTO) {
        redisTemplate.opsForZSet().remove(SCHEDULE_NOTIFICATION_KEY, notificationDTO);
    }

    public NotificationDTO getNotificationByScheduleId(Long scheduleId) {
        Set<ZSetOperations.TypedTuple<Object>> notifications = redisTemplate.opsForZSet().rangeByScoreWithScores(SCHEDULE_NOTIFICATION_KEY, 0, Double.MAX_VALUE);
        if (notifications == null) {
            return null;
        }

        return notifications.stream()
                .map(tuple -> (NotificationDTO) tuple.getValue())
                .filter(notificationDTO -> notificationDTO.getScheduleId().equals(scheduleId))
                .findFirst()
                .orElse(null);
    }
}
