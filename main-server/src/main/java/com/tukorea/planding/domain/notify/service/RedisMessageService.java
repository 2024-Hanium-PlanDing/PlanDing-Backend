package com.tukorea.planding.domain.notify.service;

import com.tukorea.planding.domain.notify.dto.NotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisMessageService {

    private static final String CHANNEL_PREFIX = "notification.user.";
    private final RedisMessageListenerContainer container;
    private final NotificationSubscriber subscriber;
    private final RedisTemplate<String, Object> redisTemplate;

    // 채널 구독
    public void subscribe(String channel) {
        container.addMessageListener(subscriber, ChannelTopic.of(getChannelName(channel)));
    }

    // 이벤트 발행
    public void publish(String channel, NotificationDTO notificationDto) {
        redisTemplate.convertAndSend(getChannelName(channel), notificationDto);
    }

    // 구독 삭제
    public void removeSubscribe(String channel) {
        container.removeMessageListener(subscriber, ChannelTopic.of(getChannelName(channel)));
    }

    private String getChannelName(String userCode) {
        return CHANNEL_PREFIX + userCode;
    }
}
