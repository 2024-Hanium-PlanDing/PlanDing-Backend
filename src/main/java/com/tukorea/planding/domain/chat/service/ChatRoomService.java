package com.tukorea.planding.domain.chat.service;

import com.tukorea.planding.domain.chat.dto.ChatRoom;
import com.tukorea.planding.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    public void createChatRoomForGroup(String groupCode) {
        ChatRoom chatRoom = chatRoomRepository.createChatRoom(groupCode);
        chatRoomRepository.enterChatRoom(groupCode);
    }
}
