package com.tukorea.planding.domain.notify.service;

import com.tukorea.planding.domain.notify.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;

@Component
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
}
