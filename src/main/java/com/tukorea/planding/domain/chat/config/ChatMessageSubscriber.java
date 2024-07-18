package com.tukorea.planding.domain.chat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tukorea.planding.domain.chat.dto.ChatMessageDTO;
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
            String channel = new String(message.getChannel())
                    .substring("chat.room.".length());
            log.info("Chat Message To {}", channel);
            ChatMessageDTO chatMessageDTO = objectMapper.readValue(message.getBody(), ChatMessageDTO.class);
            messageTemplate.convertAndSend("/sub/chat/room/" + channel, chatMessageDTO);
        } catch (IOException e) {
            log.error("Failed to handle scheduleCount", e);
        }
    }
}
