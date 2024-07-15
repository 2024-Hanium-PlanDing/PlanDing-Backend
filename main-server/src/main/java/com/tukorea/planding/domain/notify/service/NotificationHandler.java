package com.tukorea.planding.domain.notify.service;

import com.tukorea.planding.domain.notify.dto.DailyNotificationDto;
import com.tukorea.planding.domain.notify.dto.NotificationDTO;
import com.tukorea.planding.domain.notify.entity.Notification;
import com.tukorea.planding.domain.notify.entity.NotificationType;
import com.tukorea.planding.domain.notify.repository.NotificationRepository;
import com.tukorea.planding.domain.notify.service.sse.SseEmitterService;
import com.tukorea.planding.domain.schedule.dto.response.PersonalScheduleResponse;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationHandler {

    private final SseEmitterService sseEmitterService;
    private final RedisMessageService redisMessageService;



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


}
