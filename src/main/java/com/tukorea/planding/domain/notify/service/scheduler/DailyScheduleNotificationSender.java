package com.tukorea.planding.domain.notify.service.scheduler;

import com.tukorea.planding.domain.notify.dto.DailyNotificationDto;
import com.tukorea.planding.domain.notify.dto.NotificationDTO;
import com.tukorea.planding.domain.notify.entity.Notification;
import com.tukorea.planding.domain.notify.entity.NotificationType;
import com.tukorea.planding.domain.notify.repository.NotificationRepository;
import com.tukorea.planding.domain.notify.service.RedisMessageService;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleType;
import com.tukorea.planding.domain.schedule.repository.PersonalScheduleRepository;
import com.tukorea.planding.domain.schedule.repository.ScheduleRepository;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class DailyScheduleNotificationSender {

    private final ScheduleRepository scheduleRepository;
    private final UserQueryService userQueryService;
    private final NotificationRepository notificationRepository;
    private final RedisMessageService redisMessageService;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void sendDailyScheduleNotifications() {
        LocalDate today = LocalDate.now();
        try {
            List<Schedule> schedules = scheduleRepository.findAllByScheduleDate(today);


            if (schedules.isEmpty()) {
                return;
            }


            Map<User, List<Schedule>> schedulesByUser = schedules.stream()
                    .filter(schedule -> schedule.getPersonalSchedule() != null)
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
                        .message(String.format("오늘의 일정: 개인 %d개, 그룹 %d개", personalCount, groupCount))
                        .url("/api/v1/common/schedule/today")
                        .date(today)
                        .build();

                sendDailyNotification(notification);
                log.info("알림 전송 완료: 사용자 코드 = {}, 메시지 = {}", user.getUserCode(), notification.message());
            });
        } catch (Exception e) {
            log.error("일일 스케줄 알림 전송 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    public void sendDailyNotification(DailyNotificationDto request) {
        try {
            if (!userQueryService.getUserByUserCode(request.userCode()).isAlarm()) {
                return;
            }

            Notification notification = Notification.builder()
                    .userCode(request.userCode())
                    .notificationType(NotificationType.DAILY)
                    .scheduleDate(request.date())
                    .message(request.message())
                    .build();

            notificationRepository.save(notification);
            redisMessageService.publish(request.userCode(), NotificationDTO.createDailySchedule(request.userCode(), request.message(), null, String.valueOf(request.date()), null));
        } catch (Exception e) {
            log.error("정시 스케줄 알람 전송 실패 to user {}:{}", request.userCode(), e.getMessage(), e);
        }

    }

}
