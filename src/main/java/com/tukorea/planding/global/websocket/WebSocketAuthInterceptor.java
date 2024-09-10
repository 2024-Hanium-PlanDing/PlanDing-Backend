package com.tukorea.planding.global.websocket;

import com.tukorea.planding.global.config.websocket.WebSocketAuthService;
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

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final WebSocketAuthService webSocketAuthService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        try {
            StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                webSocketAuthService.handleConnect(accessor);
                log.info("CON");
            } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                webSocketAuthService.handleDisconnect(accessor);
            } else if (StompCommand.SEND.equals(accessor.getCommand())) {
                log.info("PUB: {}", accessor.getDestination());
            }
        }catch (Exception e){
            log.error("웹소켓 인터셉터 오류 메시지: {}",e);
        }

        return message;
    }
}
