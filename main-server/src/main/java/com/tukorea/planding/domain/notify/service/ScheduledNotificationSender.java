package com.tukorea.planding.domain.notify.service;

import com.tukorea.planding.domain.notify.dto.DailyNotificationDto;
import com.tukorea.planding.domain.notify.dto.NotificationDTO;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleType;
import com.tukorea.planding.domain.schedule.repository.ScheduleRepository;
import com.tukorea.planding.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduledNotificationSender {

    private final ScheduleNotificationService scheduleNotificationService;
    private final NotificationHandler notificationHandler;
    private final ScheduleRepository scheduleRepository;

    @Scheduled(fixedRate = 60000)  // 1분마다 실행
    @Transactional
    public void sendScheduleNotifications() {

        LocalDateTime now = LocalDateTime.now();
        Set<ZSetOperations.TypedTuple<Object>> dueNotifications = scheduleNotificationService.getDueNotifications();

        for (ZSetOperations.TypedTuple<Object> tuple : dueNotifications) {
            NotificationDTO notification = (NotificationDTO) tuple.getValue();

            Number scoreNumber = tuple.getScore();
            long score = scoreNumber.longValue();
            LocalDateTime decodedDateTime = LocalDateTime.ofEpochSecond(score, 0, ZoneOffset.UTC);


            // 1시간 전 로직
            if (decodedDateTime.toLocalDate().equals(now.toLocalDate()) && decodedDateTime.getHour() == now.getHour()) {
                notificationHandler.sendPersonalNotification(notification.getUserCode(), notification);
                scheduleNotificationService.removeNotification(notification);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Transactional
    public void sendDailyScheduleNotifications() {
        LocalDate today = LocalDate.now();
        List<Schedule> schedules = scheduleRepository.findAllByScheduleDate(today);

        Map<User, List<Schedule>> schedulesByUser = schedules.stream()
                .collect(Collectors.groupingBy(schedule -> schedule.getPersonalSchedule().getUser()));

        schedulesByUser.forEach((user, userSchedules) -> {
            long personalCount = userSchedules.stream()
                    .filter(schedule -> schedule.getType() == ScheduleType.PERSONAL)
                    .count();

            long groupCount = userSchedules.stream()
                    .filter(schedule -> schedule.getType() == ScheduleType.GROUP)
                    .count();

            DailyNotificationDto notification = DailyNotificationDto.builder()
                    .userCode(user.getUserCode())
                    .message("오늘의 일정: 개인 " + personalCount + "개, 그룹 " + groupCount + "개")
                    .url("/api/v1/common/schedule/today")
                    .date(today)
                    .build();

            notificationHandler.sendDailyNotification(user.getUserCode(), notification);
        });
    }


}
