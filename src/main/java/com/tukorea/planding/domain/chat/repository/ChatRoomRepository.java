package com.tukorea.planding.domain.chat.repository;

import com.tukorea.planding.domain.chat.config.ChatMessageSubscriber;
import com.tukorea.planding.domain.chat.dto.ChatRoom;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {
    private static final String CHAT_ROOMS = "chat.room.";
    private final RedisMessageListenerContainer redisMessageListener; // 채팅방에 발생되는 메시지를 처리할 Listender
    private final ChatMessageSubscriber chatMessageSubscriber; // 구독 처리서비스
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    public ChatRoom findRoomById(String id) {
        return opsHashChatRoom.get(CHAT_ROOMS, id);
    }

    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        opsHashChatRoom.put(CHAT_ROOMS + name, chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    public void enterChatRoom(String groupCode) {
        ChannelTopic topic = topics.get(groupCode);
        if (topic == null) {
            topic = new ChannelTopic(groupCode);
            redisMessageListener.addMessageListener(chatMessageSubscriber, topic);
            topics.put(groupCode, topic);
        }
    }

    public void leaveChatRoom(String groupCode){
        ChannelTopic topic = topics.get(groupCode);
        if (topic != null) {
            redisMessageListener.removeMessageListener(chatMessageSubscriber, topic);
            topics.remove(groupCode);
        }
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }

}
