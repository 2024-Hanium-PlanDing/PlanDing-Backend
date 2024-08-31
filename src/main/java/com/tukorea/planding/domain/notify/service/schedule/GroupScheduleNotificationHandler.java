package com.tukorea.planding.domain.notify.service.schedule;

import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.notify.dto.alarm.NotificationDTO;
import com.tukorea.planding.domain.notify.entity.Notification;
import com.tukorea.planding.domain.notify.entity.NotificationType;
import com.tukorea.planding.domain.notify.repository.NotificationRepository;
import com.tukorea.planding.domain.notify.service.RedisMessageService;
import com.tukorea.planding.domain.notify.service.ScheduleNotificationService;
import com.tukorea.planding.domain.notify.service.fcm.FCMService;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleStatus;
import com.tukorea.planding.domain.schedule.repository.GroupScheduleAttendanceRepository;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class GroupScheduleNotificationHandler implements NotificationHandler {

    private final RedisMessageService redisMessageService;
    private final NotificationRepository notificationRepository;
    private final ScheduleNotificationService scheduleNotificationService;
    private final GroupQueryService groupQueryService;
    private final GroupScheduleAttendanceRepository attendanceRepository;
    private final FCMService fcmService;


    @Override
    public void sendNotification(NotificationDTO request) {
        try {
            validateNotificationRequest(request);

            Notification notification = Notification.builder()
                    .userCode(request.getUserCode())
                    .groupName(request.getGroupName())
                    .notificationType(NotificationType.GROUP_SCHEDULE)
                    .scheduleDate(LocalDate.parse(request.getDate()))
                    .title(request.getTitle())
                    .message(request.getMessage())
                    .url(request.getUrl())
                    .build();

            notificationRepository.save(notification);
            // SSE
            String channel = request.getUserCode();
            redisMessageService.publish(channel, request);
            // FCM
            fcmService.groupPublish(request);
        } catch (BusinessException e) {
            log.warn("[Group Schedule] 알람 전송 실패 - 접근 권한 없음 to userCodes {}:{}", request.getUserCode(), e.getMessage());
        } catch (Exception e) {
            log.error("[Group Schedule] 알람 전송 실패 to userCodes {}:{}", request.getUserCode(), e.getMessage(), e);
        }
    }


    public void registerScheduleBeforeOneHour(String userCode, Schedule response) {
        // 1시간 전 알림
        try {
            LocalTime starTime = LocalTime.of(response.getStartTime(), 0);
            LocalDateTime oneHourBefore = LocalDateTime.of(response.getScheduleDate(), starTime).minusHours(1);
            NotificationDTO oneHourBeforeNotification = NotificationDTO.createGroupSchedule(userCode, response);
            scheduleNotificationService.scheduleNotification(oneHourBeforeNotification, oneHourBefore);
        } catch (Exception e) {
            log.error("[Group Schedule] 1시간 전 알림 등록 실패 for userCodes {}: {}", userCode, e.getMessage(), e);
        }
    }

    public void updateScheduleBeforeOneHour(String userCode, Schedule schedule) {
        try {
            NotificationDTO notification = scheduleNotificationService.getNotificationByScheduleId(schedule.getId());
            if (notification != null) {
                scheduleNotificationService.removeNotification(notification);
                log.info("[Group Schedule] 기존 그룹 알람 삭제 성공 scheduleId: {}, message: {}", schedule.getId(), notification.getMessage());
            }

            LocalTime starTime = LocalTime.of(schedule.getStartTime(), 0);
            LocalDateTime oneHourBefore = LocalDateTime.of(schedule.getScheduleDate(), starTime).minusHours(1);
            NotificationDTO oneHourBeforeNotification = NotificationDTO.createGroupSchedule(userCode, schedule);
            scheduleNotificationService.scheduleNotification(oneHourBeforeNotification, oneHourBefore);
        } catch (Exception e) {
            log.error("[Group Schedule] 1시간 전 알림 업데이트 실패 for userCodes {}: {}", userCode, e.getMessage(), e);
        }
    }

    public void deleteScheduleBeforeOneHour(Long scheduleId) {
        try {
            NotificationDTO notification = scheduleNotificationService.getNotificationByScheduleId(scheduleId);
            if (notification != null) {
                scheduleNotificationService.removeNotification(notification);
            }
        } catch (Exception e) {
            log.error("[Group Schedule] 1시간 전 알림 삭제 실패 scheduleId: {}", scheduleId, e.getMessage(), e);
        }
    }

    private void validateNotificationRequest(NotificationDTO request) {
        checkUserAccess(request);
        checkGroupAlarm(request);
        checkAttendanceStatus(request);
    }

    private void checkUserAccess(NotificationDTO request) {
        if (!groupQueryService.existGroupInUser(request.getGroupCode(), request.getUserCode())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
    }

    private void checkGroupAlarm(NotificationDTO request) {
        if (!groupQueryService.getGroupByCode(request.getGroupCode()).isAlarm()) {
            throw new BusinessException(ErrorCode.ALARM_NOT_FOUND);
        }
    }

    private void checkAttendanceStatus(NotificationDTO request) {
        boolean isPossible = attendanceRepository.findByUser_UserCodeAndSchedule_IdAndStatus(
                request.getUserCode(), request.getScheduleId(), ScheduleStatus.POSSIBLE).isPresent();
        if (!isPossible) {
            throw new BusinessException(ErrorCode.INVALID_ATTENDANCE_STATUS);
        }
    }
}
