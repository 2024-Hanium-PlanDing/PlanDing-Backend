package com.tukorea.planding.domain.notify.service.sse;

import com.tukorea.planding.domain.notify.dto.alarm.NotificationDTO;
import com.tukorea.planding.domain.notify.repository.EmitterRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseEmitterService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepositoryImpl emitterRepository;

    public SseEmitter createEmitter(String emitterKey) {
        return emitterRepository.save(emitterKey, new SseEmitter(DEFAULT_TIMEOUT));
    }

    public void deleteEmitter(String emitterKey) {
        log.info("emitter 삭제: {}",emitterKey);
        emitterRepository.deleteById(emitterKey);
    }

    public void sendNotificationToClient(String emitterKey, NotificationDTO notificationDTO) {
        log.info("sendNotificationToClient 동작 {}",emitterKey);
        emitterRepository.findById(emitterKey)
                .ifPresent(emitter -> send(notificationDTO, emitterKey, emitter));
    }

    public void send(Object data, String emitterKey, SseEmitter sseEmitter) {
        try {
            log.info("send to client {}:[{}]", emitterKey, data);
            sseEmitter.send(SseEmitter.event()
                    .id(emitterKey)
                    .data(data, MediaType.APPLICATION_JSON));
        } catch (IOException | IllegalStateException e) {
            log.error("IOException | IllegalStateException is occurred. ", e);
            emitterRepository.deleteById(emitterKey);
        }
    }

}
