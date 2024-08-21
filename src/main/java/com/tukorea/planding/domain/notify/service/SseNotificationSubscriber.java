package com.tukorea.planding.domain.notify.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tukorea.planding.domain.notify.dto.alarm.NotificationDTO;
import com.tukorea.planding.domain.notify.service.sse.SseEmitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor
public class SseNotificationSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SseEmitterService sseEmitterService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(message.getChannel())
                    .substring("notification.userCodes.".length());
            log.info("Received scheduleCount on channel: {}", channel); // 채널 이름 로깅
            NotificationDTO notification = objectMapper.readValue(message.getBody(), NotificationDTO.class);
            sseEmitterService.sendNotificationToClient(channel, notification);
        } catch (IOException e) {
            log.error("Failed to handle scheduleCount", e);
        }
    }
}
