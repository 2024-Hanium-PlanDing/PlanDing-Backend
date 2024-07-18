package com.tukorea.planding.domain.notify.service.schedule;

import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.notify.dto.NotificationDTO;
import com.tukorea.planding.domain.notify.entity.Notification;
import com.tukorea.planding.domain.notify.entity.NotificationType;
import com.tukorea.planding.domain.notify.repository.NotificationRepository;
import com.tukorea.planding.domain.notify.service.RedisMessageService;
import com.tukorea.planding.domain.notify.service.ScheduleNotificationService;
import com.tukorea.planding.domain.schedule.dto.response.ScheduleResponse;
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
public class GroupScheduleNotificationHandler {

    private final RedisMessageService redisMessageService;
    private final NotificationRepository notificationRepository;
    private final ScheduleNotificationService scheduleNotificationService;
    private final GroupQueryService groupQueryService;

    public void sendGroupNotification(String groupCode, String userCode, NotificationDTO request) {
        try {
            if (groupQueryService.existGroupInUser(userCode, groupCode)) {
                throw new BusinessException(ErrorCode.ACCESS_DENIED);
            }

            if (!groupQueryService.getGroupByCode(groupCode).isAlarm()) {
                return;
            }

            Notification notification = Notification.builder()
                    .userCode(userCode)
                    .groupName(request.getGroupName())
                    .notificationType(NotificationType.GROUP_SCHEDULE)
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

    public void registerScheduleBeforeOneHour(String userCode, ScheduleResponse response) {
        // 1시간 전 알림
        try {
            LocalTime starTime = LocalTime.of(response.startTime(), 0);
            LocalDateTime oneHourBefore = LocalDateTime.of(response.scheduleDate(), starTime).minusHours(1);
            NotificationDTO oneHourBeforeNotification = NotificationDTO.createGroupSchedule(userCode, response);
            scheduleNotificationService.scheduleNotification(oneHourBeforeNotification, oneHourBefore);
        } catch (Exception e) {
            log.error("Group 스케줄 알람 등록 실패 for user {}: {}", userCode, e.getMessage(), e);
        }
    }
}
