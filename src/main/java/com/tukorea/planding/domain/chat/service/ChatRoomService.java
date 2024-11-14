package com.tukorea.planding.domain.chat.service;

import com.tukorea.planding.domain.chat.repository.ChatRoomRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@Builder
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public void createChatRoomAfterCommit(String groupCode) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                createChatRoomForGroup(groupCode);
            }
        });
    }

    public void createChatRoomForGroup(String groupCode) {
        chatRoomRepository.createChatRoom(groupCode);
        chatRoomRepository.enterChatRoom(groupCode);
    }

    public void deleteChatRoom(String groupCode) {
        chatRoomRepository.leaveChatRoom(groupCode);
    }
}
