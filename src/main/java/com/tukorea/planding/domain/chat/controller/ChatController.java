package com.tukorea.planding.domain.chat.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.chat.dto.ChatMessageDTO;
import com.tukorea.planding.domain.chat.dto.MessageResponse;
import com.tukorea.planding.domain.chat.repository.ChatRoomRepository;
import com.tukorea.planding.domain.chat.config.RedisChatService;
import com.tukorea.planding.domain.chat.service.ChatRoomService;
import com.tukorea.planding.domain.chat.service.ChatService;
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
    private final ChatService chatService;

    @MessageMapping("/chat/{groupCode}")
    @SendTo("/sub/chat/{groupCode}")
    public CommonResponse<MessageResponse> message(@Payload ChatMessageDTO messageDTO) {
        if (ChatMessageDTO.MessageType.JOIN.equals(messageDTO.getType())) {
            chatRoomRepository.enterChatRoom(messageDTO.getGroupCode());
            messageDTO.join(messageDTO);
        }

        MessageResponse messageResponse = chatService.sendMessage(messageDTO);
        redisChatService.publish(messageDTO.getGroupCode(), messageResponse);
        return CommonUtils.success(messageResponse);
    }
}
