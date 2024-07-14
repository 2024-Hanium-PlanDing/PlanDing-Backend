package com.tukorea.planding.domain.notify.service;

import com.tukorea.planding.domain.notify.dto.NotificationDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ScheduleNotificationService {


    private static final String SCHEDULE_NOTIFICATION_KEY = "schedule_notifications";
    private final RedisTemplate<String, Object> redisTemplate;
    private ZSetOperations<String, Object> zSetOperations;

    @PostConstruct
    public void init() {
        this.zSetOperations = redisTemplate.opsForZSet();
    }



    public void scheduleNotification(NotificationDTO notificationDTO, LocalDateTime notificationTime) {
        long score = notificationTime.toEpochSecond(ZoneOffset.UTC);
        redisTemplate.opsForZSet().add(SCHEDULE_NOTIFICATION_KEY, notificationDTO, score);
    }

    public Set<ZSetOperations.TypedTuple<Object>> getDueNotifications(LocalDateTime now) {
        long nowEpoch = now.atZone(ZoneId.systemDefault()).toEpochSecond();
        long oneMinuteLaterEpoch = now.plusMinutes(1).atZone(ZoneId.systemDefault()).toEpochSecond();

        return zSetOperations.rangeByScoreWithScores(SCHEDULE_NOTIFICATION_KEY, nowEpoch, oneMinuteLaterEpoch);
    }

    public void removeNotification(NotificationDTO notificationDTO) {
        redisTemplate.opsForZSet().remove(SCHEDULE_NOTIFICATION_KEY, notificationDTO);
    }
}
