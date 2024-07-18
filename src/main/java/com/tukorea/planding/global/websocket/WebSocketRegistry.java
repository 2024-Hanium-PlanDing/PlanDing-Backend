package com.tukorea.planding.global.websocket;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketRegistry {
    private final Map<String, UserInfoSession> sessions = new ConcurrentHashMap<>();

    public void register(String sessionId, UserInfoSession userInfoSession) {
        sessions.put(sessionId, userInfoSession);
    }

    public void unregister(String sessionId) {
        sessions.remove(sessionId);
    }

    public UserInfoSession getRegister(String sessionId) {
        return sessions.get(sessionId);
    }

}