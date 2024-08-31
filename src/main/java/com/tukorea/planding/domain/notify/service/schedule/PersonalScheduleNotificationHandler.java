package com.tukorea.planding.domain.notify.service.schedule;

import com.tukorea.planding.domain.notify.dto.alarm.NotificationDTO;
import com.tukorea.planding.domain.notify.entity.Notification;
import com.tukorea.planding.domain.notify.entity.NotificationType;
import com.tukorea.planding.domain.notify.repository.NotificationRepository;
import com.tukorea.planding.domain.notify.service.RedisMessageService;
import com.tukorea.planding.domain.notify.service.ScheduleNotificationService;
import com.tukorea.planding.domain.notify.service.fcm.FCMService;
import com.tukorea.planding.domain.schedule.dto.response.PersonalScheduleResponse;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.service.UserQueryService;
import com.tukorea.planding.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class PersonalScheduleNotificationHandler implements NotificationHandler {

    private final RedisMessageService redisMessageService;
    private final NotificationRepository notificationRepository;
    private final ScheduleNotificationService scheduleNotificationService;
    private final UserQueryService userQueryService;
    private final FCMService fcmService;

    // 개인 스케줄 알림 코드
    @Override
    public void sendNotification(NotificationDTO request) {
        try {
            User user = userQueryService.getUserByUserCode(request.getUserCode());
            if (!user.isAlarm()) {
                return;
            }

            Notification notification = Notification.builder()
                    .userCode(request.getUserCode())
                    .title(request.getTitle())
                    .notificationType(NotificationType.PERSONAL_SCHEDULE)
                    .scheduleDate(LocalDate.parse(request.getDate()))
                    .message(request.getMessage())
                    .url(request.getUrl())
                    .build();

            notificationRepository.save(notification);
            // SSE
            String channel = request.getUserCode();
            redisMessageService.publish(channel, request);
            // FCM
            fcmService.personalPublish(request);
            log.info("[Personal Schedule] 알람 전송 성공 to userCodes {}", request.getUserCode());
        } catch (BusinessException e) {
            log.warn("[Personal Schedule] 알람 전송 실패 - 접근 권한 없음 to userCodes {}:{}", request.getUserCode(), e.getMessage());
        } catch (Exception e) {
            log.error("[Personal Schedule] 알람 전송 실패 to userCodes {}:{}", request.getUserCode(), e.getMessage(), e);
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
            log.error("정시 스케줄 알람 등록 실패 for userCodes {}: {}", userCode, e.getMessage(), e);
        }

    }

    public void registerScheduleBeforeOneHour(String userCode, PersonalScheduleResponse request) {
        // 1시간 전 알림
        try {
            LocalTime starTime = LocalTime.of(request.startTime(), 0);
            LocalDateTime oneHourBefore = LocalDateTime.of(request.scheduleDate(), starTime).minusHours(1);
            NotificationDTO oneHourBeforeNotification = NotificationDTO.createPersonalSchedule(userCode, request);
            scheduleNotificationService.scheduleNotification(oneHourBeforeNotification, oneHourBefore);
        } catch (Exception e) {
            log.error("[Personal Schedule] 1시간 전 알림 등록 실패 for userCodes {}: {}", userCode, e.getMessage(), e);
        }
    }

    public void updateScheduleBeforeOneHour(Long scheduleId, PersonalScheduleResponse request, UserInfo userInfo) {
        try {
            NotificationDTO notification = scheduleNotificationService.getNotificationByScheduleId(scheduleId);
            if (notification != null) {
                scheduleNotificationService.removeNotification(notification);
                log.info("[Personal Schedule] 기존 그룹 알람 삭제 성공 scheduleId: {}, message: {}", scheduleId, notification.getMessage());
            }

            LocalTime starTime = LocalTime.of(request.startTime(), 0);
            LocalDateTime oneHourBefore = LocalDateTime.of(request.scheduleDate(), starTime).minusHours(1);
            NotificationDTO oneHourBeforeNotification = NotificationDTO.createPersonalSchedule(userInfo.getUserCode(), request);
            scheduleNotificationService.scheduleNotification(oneHourBeforeNotification, oneHourBefore);
        } catch (Exception e) {
            log.error("[Personal Schedule] 1시간 전 알림 업데이트 실패 for userCodes {}: {}", userInfo.getUserCode(), e.getMessage(), e);
        }
    }

    public void deleteScheduleBeforeOneHour(Long scheduleId) {
        try {
            NotificationDTO notification = scheduleNotificationService.getNotificationByScheduleId(scheduleId);
            if (notification != null) {
                scheduleNotificationService.removeNotification(notification);
            }
        } catch (Exception e) {
            log.error("[Personal Schedule] 1시간 전 알림 삭제 실패 scheduleId: {}", scheduleId, e.getMessage(), e);
        }

    }
}
