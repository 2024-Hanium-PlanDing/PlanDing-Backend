package com.tukorea.planding.domain.notify.service;

import com.tukorea.planding.domain.notify.dto.NotificationDTO;
import com.tukorea.planding.domain.notify.entity.Notification;
import com.tukorea.planding.domain.notify.entity.NotificationType;
import com.tukorea.planding.domain.notify.repository.NotificationRepository;
import com.tukorea.planding.domain.notify.service.setting.UserNotificationSettingQueryService;
import com.tukorea.planding.domain.notify.service.sse.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SseEmitterService sseEmitterService;
    private final RedisMessageService redisMessageService;
    private final UserNotificationSettingQueryService userNotificationSettingQueryService;
    private final NotificationRepository notificationRepository;


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
                .createdAt(LocalDateTime.now())
                .message(request.getMessage())
                .url(request.getUrl())
//                .scheduleTime(LocalDateTime.of(LocalDate.parse(request.getCreatedAt().substring(0, 10)), LocalTime.parse(request.getCreatedAt().substring(11))))
                .build();

        notificationRepository.save(notification);

        String channel = request.getUserCode();
        redisMessageService.publish(channel, request);
    }
}
