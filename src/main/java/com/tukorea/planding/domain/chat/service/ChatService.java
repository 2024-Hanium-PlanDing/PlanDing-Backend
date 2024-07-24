package com.tukorea.planding.domain.chat.service;

import com.tukorea.planding.domain.chat.dto.ChatMessageDTO;
import com.tukorea.planding.domain.chat.dto.MessageResponse;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final UserQueryService userQueryService;

    public MessageResponse sendMessage(ChatMessageDTO messageDTO) {
        User user = userQueryService.getUserByUserCode(messageDTO.getSender());
        return MessageResponse.builder()
                .message(messageDTO.getContent())
                .name(user.getUsername())
                .profileImage(user.getProfileImage())
                .createdAt(LocalDateTime.now())
                .build();

    }
}
