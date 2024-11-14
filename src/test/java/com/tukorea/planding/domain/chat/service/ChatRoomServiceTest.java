package com.tukorea.planding.domain.chat.service;

import com.tukorea.planding.mock.FakeChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.junit.jupiter.api.Assertions.*;

class ChatRoomServiceTest {

    private ChatRoomService chatRoomService;
    private FakeChatRoomRepository fakeChatRoomRepository;

    @BeforeEach
    void init() {
        this.fakeChatRoomRepository = new FakeChatRoomRepository();
        this.chatRoomService = ChatRoomService.builder()
                .chatRoomRepository(fakeChatRoomRepository)
                .build();
    }

    @Test
    void testCreateChatRoomAfterCommit() {
        String groupCode = "testGroup";

        TransactionSynchronizationManager.initSynchronization();
        chatRoomService.createChatRoomAfterCommit(groupCode);
        TransactionSynchronizationManager.clearSynchronization();

        assertNotNull(fakeChatRoomRepository.findByGroupCode(groupCode));
        assertTrue(fakeChatRoomRepository.isUserInChatRoom(groupCode));
    }

    @Test
    void testDeleteChatRoom() {
        String groupCode = "testGroup";
        fakeChatRoomRepository.createChatRoom(groupCode);
        chatRoomService.deleteChatRoom(groupCode);

        assertFalse(fakeChatRoomRepository.isUserInChatRoom(groupCode));
    }

}