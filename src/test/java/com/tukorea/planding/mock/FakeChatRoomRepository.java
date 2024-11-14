package com.tukorea.planding.mock;

import com.tukorea.planding.domain.chat.dto.ChatRoom;
import com.tukorea.planding.domain.chat.repository.ChatRoomRepository;

import java.util.HashMap;
import java.util.Map;

public class FakeChatRoomRepository implements ChatRoomRepository {

    private final Map<String, ChatRoom> chatRooms = new HashMap<>();

    @Override
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);  // 채팅방 생성
        chatRooms.put(chatRoom.getRoomId(), chatRoom);  // 생성된 채팅방을 메모리 맵에 저장
        return chatRoom;
    }

    @Override
    public void enterChatRoom(String groupCode) {
        if (!chatRooms.containsKey(groupCode)) {
            throw new IllegalArgumentException("Chat room with groupCode " + groupCode + " does not exist.");
        }
        // 테스트 목적이므로 구체적인 입장 로직 없이 존재 여부만 확인합니다.
    }

    @Override
    public void leaveChatRoom(String groupCode) {
        chatRooms.remove(groupCode);  // 메모리 맵에서 해당 채팅방 제거
    }

    // 추가로 테스트에서 채팅방 상태를 검증할 수 있도록 유틸리티 메서드 제공
    public boolean exists(String groupCode) {
        return chatRooms.containsKey(groupCode);
    }

    public int countChatRooms() {
        return chatRooms.size();
    }

    public ChatRoom findByGroupCode(String groupCode) {
        return chatRooms.get(groupCode);
    }

    public boolean isUserInChatRoom(String groupCode) {
        return chatRooms.containsKey(groupCode);
    }
}