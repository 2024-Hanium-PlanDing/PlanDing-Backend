package com.tukorea.planding.domain.chat.service;

import com.tukorea.planding.domain.chat.config.RedisChatService;
import com.tukorea.planding.domain.chat.dto.MessageRequest;
import com.tukorea.planding.domain.chat.dto.MessageResponse;
import com.tukorea.planding.domain.chat.entity.ChatMessage;
import com.tukorea.planding.domain.chat.repository.ChatMessageRepository;
import com.tukorea.planding.domain.group.service.query.UserGroupQueryService;
import com.tukorea.planding.domain.user.dto.UserResponse;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.domain.user.service.UserQueryService;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final UserQueryService userQueryService;
    private final RedisChatService redisChatService;
    private final ChatMessageRepository chatMessageRepository;
    private final UserGroupQueryService userGroupQueryService;

    public MessageResponse sendMessage(String sender, MessageRequest messageDTO, String groupCode) {
        UserDomain user = userQueryService.getUserByUserCode(sender);

        ChatMessage chatMessage = ChatMessage.builder()
                .content(messageDTO.getContent())
                .sender(sender)
                .groupCode(groupCode)
                .createdAt(LocalDateTime.now())
                .build();
        ChatMessage save = chatMessageRepository.save(chatMessage);

        MessageResponse response = MessageResponse.builder()
                .id(save.getId())
                .userCode(user.getUserCode())
                .message(messageDTO.getContent())
                .name(user.getUsername())
                .profileImage(user.getProfileImage())
                .createdAt(LocalDateTime.now())
                .build();

        redisChatService.publish(groupCode, response);
        return response;
    }

    public List<MessageResponse> getMessages(UserResponse userResponse, String groupCode) {
        if (!userGroupQueryService.checkUserAccessToGroupRoom(groupCode, userResponse.getUserCode())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        List<ChatMessage> messages = chatMessageRepository.findByGroupCode(groupCode);
        return messages.stream()
                .map(message -> MessageResponse.builder()
                        .id(message.getId())
                        .userCode(message.getSender())
                        .message(message.getContent())
                        .name(userQueryService.getUserByUserCode(message.getSender()).getUsername())
                        .profileImage(userQueryService.getUserByUserCode(message.getSender()).getProfileImage())
                        .createdAt(message.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
