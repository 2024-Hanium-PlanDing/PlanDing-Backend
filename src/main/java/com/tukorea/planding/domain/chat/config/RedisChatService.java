package com.tukorea.planding.domain.chat.config;

import com.tukorea.planding.domain.chat.dto.ChatMessageDTO;
import com.tukorea.planding.domain.chat.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisChatService {

    private static final String CHANNEL_PREFIX = "chat.room.";
    private final RedisMessageListenerContainer container;
    private final ChatMessageSubscriber subscriber;
    private final RedisTemplate<String, Object> redisTemplate;

    // 채널 구독
    public void subscribe(String channel) {
        container.addMessageListener(subscriber, ChannelTopic.of(getChannelName(channel)));
    }

    // 메시지 발행
    public void publish(String channel, MessageResponse response) {
        redisTemplate.convertAndSend(getChannelName(channel), response);
    }

    // 구독 삭제
    public void removeSubscribe(String channel) {
        container.removeMessageListener(subscriber, ChannelTopic.of(getChannelName(channel)));
    }

    private String getChannelName(String roomId) {
        return CHANNEL_PREFIX + roomId;
    }
}
