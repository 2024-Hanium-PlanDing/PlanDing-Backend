package com.tukorea.planding.domain.chat.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.chat.config.RedisChatService;
import com.tukorea.planding.domain.chat.dto.MessageRequest;
import com.tukorea.planding.domain.chat.dto.MessageResponse;
import com.tukorea.planding.domain.chat.service.ChatService;
import com.tukorea.planding.domain.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/{groupCode}")
    public CommonResponse<MessageResponse> sendMessage(@AuthenticationPrincipal UserInfo userInfo, @PathVariable String groupCode, @RequestBody MessageRequest request) {
        MessageResponse messageResponse = chatService.sendMessage(userInfo.getUserCode(), request, groupCode);
        return CommonUtils.success(messageResponse);
    }

    @GetMapping("/{groupCode}/message")
    public CommonResponse<List<MessageResponse>> getMessages(@AuthenticationPrincipal UserInfo userInfo, @PathVariable String groupCode) {
        List<MessageResponse> responses = chatService.getMessages(userInfo, groupCode);
        return CommonUtils.success(responses);
    }
}
