package com.tukorea.planding.global.config.websocket;

import com.tukorea.planding.domain.group.service.UserGroupService;
import com.tukorea.planding.global.config.security.jwt.JwtTokenHandler;
import com.tukorea.planding.global.websocket.UserInfoSession;
import com.tukorea.planding.global.websocket.WebSocketRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketAuthService {

    private final JwtTokenHandler jwtTokenHandler;
    private final WebSocketRegistry webSocketRegistry;
    private final UserGroupService userGroupService;

    /*
    웹소켓 연결시 웹소켓 세션과 유저의 정보를 관리하기 위해 3가지 정보를 추출
    sessionId, token, groupCode
     */
    public void handleConnect(StompHeaderAccessor accessor) {
        try {
            String sessionId = accessor.getSessionId();
            String jwt = accessor.getFirstNativeHeader("Authorization");
            String groupCode = accessor.getFirstNativeHeader("groupCode");

            if(jwt==null){
                log.error("오류 테스트 {} ",jwt);
            }

            if (jwt != null && jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
                if (jwtTokenHandler.validateToken(jwt)) {
                    String userCode = jwtTokenHandler.extractClaim(jwt, claims -> claims.get("code", String.class));
                    // 유저코드 저장
                    accessor.getSessionAttributes().put("userCode", userCode);
                    // 웹소켓 세션설정
                    webSocketRegistry.register(sessionId, new UserInfoSession(userCode, groupCode));
                    // 유저 그룹 접속 업데이트
                    userGroupService.updateConnectionStatus(userCode, groupCode, true);
                }
            } else {
                log.error("WebSocket 에러: JWT token not found or invalid format");
            }
        }catch (Exception e){
            log.error("handleConnect: {}",e);
        }
    }

    public void handleDisconnect(StompHeaderAccessor accessor) {
        try {
            String sessionId = accessor.getSessionId();
            UserInfoSession userInfo = webSocketRegistry.getRegister(sessionId);

            if (userInfo != null) {
                userGroupService.updateConnectionStatus(userInfo.userCode(), userInfo.groupCode(), false);
            }

            webSocketRegistry.unregister(sessionId);
        }catch (Exception e){
            log.error("handleDisconnect Error: {}",e);
        }
    }

}
