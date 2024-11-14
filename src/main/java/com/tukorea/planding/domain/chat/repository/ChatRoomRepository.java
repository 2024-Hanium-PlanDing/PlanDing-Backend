package com.tukorea.planding.domain.chat.repository;

import com.tukorea.planding.domain.chat.dto.ChatRoom;

public interface ChatRoomRepository {

    ChatRoom createChatRoom(String name);

    void enterChatRoom(String groupCode);

    void leaveChatRoom(String groupCode);
}