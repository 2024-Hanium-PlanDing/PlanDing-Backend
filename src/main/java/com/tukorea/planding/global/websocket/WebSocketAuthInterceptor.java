package com.tukorea.planding.global.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tukorea.planding.domain.group.service.UserGroupService;
import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.schedule.dto.request.GroupScheduleRequest;
import com.tukorea.planding.domain.schedule.entity.Action;
import com.tukorea.planding.global.config.security.jwt.JwtTokenHandler;
import com.tukorea.planding.global.config.websocket.WebSocketAuthService;
import io.swagger.v3.core.util.ObjectMapperFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final WebSocketAuthService webSocketAuthService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            webSocketAuthService.handleConnect(accessor);
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            webSocketAuthService.handleDisconnect(accessor);
        } else if (StompCommand.SEND.equals(accessor.getCommand())) {
            log.info("PUB: {}", accessor.getDestination());
        }

        return message;
    }
}
