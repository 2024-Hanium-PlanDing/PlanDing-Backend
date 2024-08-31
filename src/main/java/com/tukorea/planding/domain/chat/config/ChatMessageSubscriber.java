package com.tukorea.planding.domain.chat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.chat.dto.MessageRequest;
import com.tukorea.planding.domain.chat.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatMessageSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messageTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("Received Redis message: {}", new String(message.getBody()));  // 추가된 로그
            String channel = new String(message.getChannel())
                    .substring("chat.room.".length());
            MessageResponse messageResponse = objectMapper.readValue(message.getBody(), MessageResponse.class);
            messageTemplate.convertAndSend("/sub/chat/" + channel, CommonUtils.success(messageResponse));
        } catch (IOException e) {
            log.error("채팅 실패: {}", e.getMessage(), e);
        }
    }
}
