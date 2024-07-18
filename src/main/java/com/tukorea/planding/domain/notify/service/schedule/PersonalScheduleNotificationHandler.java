package com.tukorea.planding.domain.notify.service.schedule;

import com.tukorea.planding.domain.notify.dto.DailyNotificationDto;
import com.tukorea.planding.domain.notify.dto.NotificationDTO;
import com.tukorea.planding.domain.notify.entity.Notification;
import com.tukorea.planding.domain.notify.entity.NotificationType;
import com.tukorea.planding.domain.notify.repository.NotificationRepository;
import com.tukorea.planding.domain.notify.service.RedisMessageService;
import com.tukorea.planding.domain.notify.service.ScheduleNotificationService;
import com.tukorea.planding.domain.schedule.dto.response.PersonalScheduleResponse;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class PersonalScheduleNotificationHandler {

    private final RedisMessageService redisMessageService;
    private final NotificationRepository notificationRepository;
    private final ScheduleNotificationService scheduleNotificationService;
    private final UserQueryService userQueryService;

    // 개인 스케줄 알림 코드
    public void sendPersonalNotification(String userCode, NotificationDTO request) {
        try {
            if (!userQueryService.getUserByUserCode(userCode).isAlarm()) {
                return;
            }

            Notification notification = Notification.builder()
                    .userCode(userCode)
                    .groupName(null)
                    .notificationType(NotificationType.PERSONAL_SCHEDULE)
                    .scheduleDate(LocalDate.parse(request.getDate()))
                    .message(request.getMessage())
                    .url(request.getUrl())
                    .build();

            notificationRepository.save(notification);
            String channel = request.getUserCode();
            redisMessageService.publish(channel, request);
        } catch (Exception e) {
            log.error("알람 전송 실패 to user {}:{}", userCode, e.getMessage(), e);
        }

    }

    public void sendDailyNotification(String userCode, DailyNotificationDto request) {
        try {
            if (!userQueryService.getUserByUserCode(userCode).isAlarm()) {
                return;
            }

            Notification notification = Notification.builder()
                    .userCode(userCode)
                    .groupName(null)
                    .notificationType(NotificationType.DAILY)
                    .scheduleDate(request.date())
                    .message(request.message())
                    .url(null)
                    .build();

            notificationRepository.save(notification);
            redisMessageService.publish(userCode, NotificationDTO.createDailySchedule(userCode, request.message(), null, String.valueOf(request.date()), null));
        } catch (Exception e) {
            log.error("정시 스케줄 알람 전송 실패 to user {}:{}", userCode, e.getMessage(), e);
        }

    }

    public void registerScheduleBeforeDay(String userCode, PersonalScheduleResponse request) {
        // 자정 알림
        try {
            LocalDateTime scheduleDateTime = LocalDateTime.of(request.scheduleDate(), LocalTime.ofSecondOfDay(request.startTime()));
            LocalDateTime oneDayBefore = scheduleDateTime.withHour(0).withMinute(0).withSecond(0);
            NotificationDTO oneDayBeforeNotification = NotificationDTO.createDailySchedule(
                    userCode, request.title(), request.id(), String.valueOf(request.scheduleDate()), request.startTime());
            scheduleNotificationService.scheduleNotification(oneDayBeforeNotification, oneDayBefore);
        } catch (Exception e) {
            log.error("정시 스케줄 알람 등록 실패 for user {}: {}", userCode, e.getMessage(), e);
        }

    }

    public void registerScheduleBeforeOneHour(String userCode, PersonalScheduleResponse request) {
        // 1시간 전 알림
        try {
            LocalTime starTime = LocalTime.of(request.startTime(), 0);
            LocalDateTime oneHourBefore = LocalDateTime.of(request.scheduleDate(), starTime).minusHours(1);
            NotificationDTO oneHourBeforeNotification = NotificationDTO.createPersonalSchedule(request.id(), userCode, request);
            scheduleNotificationService.scheduleNotification(oneHourBeforeNotification, oneHourBefore);
        } catch (Exception e) {
            log.error("스케줄 알람 등록 실패 for user {}: {}", userCode, e.getMessage(), e);
        }
    }

    public void updateScheduleBeforeOneHour(Long scheduleId, PersonalScheduleResponse request, UserInfo userInfo) {
        try {
            NotificationDTO notification = scheduleNotificationService.getNotificationByScheduleId(scheduleId);
            if (notification != null) {
                scheduleNotificationService.removeNotification(notification);
                log.info("update 기존 알람 삭제 scheduleId {}: {}", scheduleId, notification.getMessage());
            }

            LocalTime starTime = LocalTime.of(request.startTime(), 0);
            LocalDateTime oneHourBefore = LocalDateTime.of(request.scheduleDate(), starTime).minusHours(1);
            NotificationDTO oneHourBeforeNotification = NotificationDTO.createPersonalSchedule(scheduleId, userInfo.getUserCode(), request);
            scheduleNotificationService.scheduleNotification(oneHourBeforeNotification, oneHourBefore);
        } catch (Exception e) {
            log.error("한 시간전 알림 업데이트 실패 scheduleId {}:{}", scheduleId, e.getMessage(), e);
        }


    }

    public void deleteScheduleBeforeOneHour(Long scheduleId) {
        try {
            NotificationDTO notification = scheduleNotificationService.getNotificationByScheduleId(scheduleId);
            if (notification != null) {
                scheduleNotificationService.removeNotification(notification);
                log.info("delete 기존 알람 삭제 scheduleId {}: {}", scheduleId, notification.getMessage());
            }
        } catch (Exception e) {
            log.error("한 시간전 알림 업데이트 실패 scheduleId {}:{}", scheduleId, e.getMessage(), e);
        }

    }
}
