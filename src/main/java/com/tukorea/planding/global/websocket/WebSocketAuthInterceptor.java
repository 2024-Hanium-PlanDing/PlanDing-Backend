package com.tukorea.planding.global.websocket;

import com.tukorea.planding.domain.group.service.UserGroupService;
import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.schedule.dto.request.GroupScheduleRequest;
import com.tukorea.planding.global.config.security.jwt.JwtTokenHandler;
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

    private final JwtTokenHandler jwtTokenHandler;
    private final WebSocketRegistry webSocketRegistry;
    private final UserGroupService userGroupService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            handleConnect(accessor);
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            handleDisconnect(accessor);
        }

        return message;
    }

    /*
    웹소켓 연결시 웹소켓 세션과 유저의 정보를 관리하기 위해 3가지 정보를 추출
    sessionId, token, groupCode
     */
    public void handleConnect(StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        String jwt = accessor.getFirstNativeHeader("Authorization");
        String groupCode = accessor.getFirstNativeHeader("groupCode");


        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
            if (jwtTokenHandler.validateToken(jwt)) {
                String userCode = jwtTokenHandler.extractClaim(jwt, claims -> claims.get("code", String.class));

                // 웹소켓 세션설정
                webSocketRegistry.register(sessionId, new UserInfoSession(userCode, groupCode));

                // 유저 그룹 접속 업데이트
                userGroupService.updateConnectionStatus(userCode, groupCode, true);
            }
        } else {
            log.error("JWT token not found or invalid format");
        }
    }

    public void handleDisconnect(StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();
        UserInfoSession userInfo = webSocketRegistry.getRegister(sessionId);

        if (userInfo != null) {
            userGroupService.updateConnectionStatus(userInfo.userCode(), userInfo.groupCode(), false);
        }

        webSocketRegistry.unregister(sessionId);
    }


}
