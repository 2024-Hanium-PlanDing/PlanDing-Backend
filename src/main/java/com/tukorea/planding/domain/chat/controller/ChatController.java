package com.tukorea.planding.domain.chat.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.chat.dto.MessageRequest;
import com.tukorea.planding.domain.chat.dto.MessageResponse;
import com.tukorea.planding.domain.chat.service.ChatService;
import com.tukorea.planding.domain.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public CommonResponse<MessageResponse> sendMessage(@AuthenticationPrincipal UserResponse userResponse, @PathVariable String groupCode, @RequestBody MessageRequest request) {
        MessageResponse messageResponse = chatService.sendMessage(userResponse.getUserCode(), request, groupCode);
        return CommonUtils.success(messageResponse);
    }

    @Operation(summary = "채팅방 내용 불러오기")
    @GetMapping("/{groupCode}/message")
    public CommonResponse<List<MessageResponse>> getMessages(@AuthenticationPrincipal UserResponse userResponse, @PathVariable String groupCode) {
        List<MessageResponse> responses = chatService.getMessages(userResponse, groupCode);
        return CommonUtils.success(responses);
    }
}
