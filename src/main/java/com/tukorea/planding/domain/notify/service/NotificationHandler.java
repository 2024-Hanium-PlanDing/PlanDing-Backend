package com.tukorea.planding.domain.notify.service;

import com.tukorea.planding.domain.notify.dto.NotificationReadRequest;
import com.tukorea.planding.domain.notify.dto.NotificationScheduleResponse;
import com.tukorea.planding.domain.notify.entity.Notification;
import com.tukorea.planding.domain.notify.repository.NotificationRepository;
import com.tukorea.planding.domain.notify.service.sse.SseEmitterService;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationHandler {

    private final SseEmitterService sseEmitterService;
    private final RedisMessageService redisMessageService;
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

    @Transactional
    public void markNotificationAsRead(NotificationReadRequest notificationReadRequest) {
        Notification notification = notificationRepository.findById(notificationReadRequest.notificationId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ALARM_NOT_FOUND));
        notification.updateRead();
    }

    @Transactional(readOnly = true)
    public List<NotificationScheduleResponse> getNotifications(UserInfo userInfo) {
        return notificationRepository.findByUserCode(userInfo.getUserCode()).stream()
                .map(NotificationScheduleResponse::of)
                .collect(Collectors.toList());
    }


    public void deleteNotification(UserInfo userInfo, Long scheduleId) {
        notificationRepository.deleteById(scheduleId);
    }
}
