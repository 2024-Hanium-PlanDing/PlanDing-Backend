package com.tukorea.planding.domain.chat.controller;

import com.tukorea.planding.domain.chat.dto.ChatMessageDTO;
import com.tukorea.planding.domain.chat.repository.ChatRoomRepository;
import com.tukorea.planding.domain.chat.config.RedisChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final RedisChatService redisChatService;
    private final ChatRoomRepository chatRoomRepository;

    @MessageMapping("/chat/{groupCode}")
    @SendTo("/sub/chat/{groupCode}")
    public ChatMessageDTO message(@Payload ChatMessageDTO messageDTO) {
        if (ChatMessageDTO.MessageType.JOIN.equals(messageDTO.getType())) {
            chatRoomRepository.enterChatRoom(messageDTO.getGroupCode());
            messageDTO.join(messageDTO);
        }
        redisChatService.publish(messageDTO.getGroupCode(), messageDTO);
        return messageDTO;
    }
}
