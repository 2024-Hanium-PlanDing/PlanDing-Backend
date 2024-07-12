package com.tukorea.planding.domain.notify.service;

import com.tukorea.planding.domain.notify.dto.DailyNotificationDto;
import com.tukorea.planding.domain.notify.dto.NotificationDTO;
import com.tukorea.planding.domain.notify.entity.Notification;
import com.tukorea.planding.domain.notify.entity.NotificationType;
import com.tukorea.planding.domain.notify.repository.NotificationRepository;
import com.tukorea.planding.domain.notify.service.setting.UserNotificationSettingQueryService;
import com.tukorea.planding.domain.notify.service.sse.SseEmitterService;
import com.tukorea.planding.domain.schedule.dto.request.PersonalScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.response.PersonalScheduleResponse;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class NotificationHandler {

    private final SseEmitterService sseEmitterService;
    private final RedisMessageService redisMessageService;
    private final UserNotificationSettingQueryService userNotificationSettingQueryService;
    private final NotificationRepository notificationRepository;
    private final ScheduleNotificationService scheduleNotificationService;


    public SseEmitter subscribe(String userCode) {
        SseEmitter sseEmitter = sseEmitterService.createEmitter(userCode);
        sseEmitterService.send("연결되었습니다. [userCode=" + userCode + "]", userCode, sseEmitter);

        sseEmitter.onTimeout(sseEmitter::complete);
        sseEmitter.onError((e) -> sseEmitter.complete());
        sseEmitter.onCompletion(() -> {
            sseEmitterService.deleteEmitter(userCode);
            redisMessageService.removeSubscribe(userCode); // 구독한 채널 삭제
        });
        return sseEmitter;
    }

    // 개인 스케줄 알림 코드
    public void sendPersonalNotification(String userCode, NotificationDTO request) {
        if (!userNotificationSettingQueryService.getSettingValue(userCode).isScheduleNotificationEnabled()) {
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
    }

    public void sendDailyNotification(String userCode, DailyNotificationDto request) {
        if (!userNotificationSettingQueryService.getSettingValue(userCode).isScheduleNotificationEnabled()) {
            return;
        }

        Notification notification = Notification.builder()
                .userCode(userCode)
                .groupName(null)
                .notificationType(NotificationType.DAILY)
                .scheduleDate(request.date())
                .message(request.message())
                .url(request.url())
                .build();

        notificationRepository.save(notification);
        redisMessageService.publish(userCode, NotificationDTO.createPersonalSchedule(userCode, request.message(), null, String.valueOf(request.date()), null));
    }

    public void scheduleNotification(String userCode, Schedule schedule) {
        LocalDateTime scheduleDateTime = LocalDateTime.of(schedule.getScheduleDate(), LocalTime.ofSecondOfDay(schedule.getStartTime()));

        // 자정 알림
        LocalDateTime oneDayBefore = scheduleDateTime.minusDays(1).withHour(0).withMinute(0).withSecond(0);
        NotificationDTO oneDayBeforeNotification = NotificationDTO.createPersonalSchedule(
                userCode, schedule.getTitle(), "/api/v1/schedule/" + schedule.getId(), String.valueOf(schedule.getScheduleDate()), String.valueOf(schedule.getStartTime()));
        scheduleNotificationService.scheduleNotification(oneDayBeforeNotification, oneDayBefore);
    }

    public void scheduleBeforeOneHour(String userCode, PersonalScheduleResponse request) {
        // 1시간 전 알림
        LocalDateTime oneHourBefore = LocalDateTime.of(request.scheduleDate(), LocalTime.ofSecondOfDay(request.startTime()));
        NotificationDTO oneHourBeforeNotification = NotificationDTO.createPersonalSchedule(
                userCode, request.title(), "/api/v1/schedule/" + request.id(), String.valueOf(request.scheduleDate()), String.valueOf(request.startTime()));
        scheduleNotificationService.scheduleNotification(oneHourBeforeNotification, oneHourBefore);
    }

}
