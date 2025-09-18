package com.alexIT.VioletsNeils.session;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserSessionManager {

    private final Map<Long, UserSession> sessions = new ConcurrentHashMap<>();

    public UserSession getOrCreateSession(Long userId) {
        return sessions.computeIfAbsent(userId, UserSession::new);
    }

    // TODO: Настроить авто удаление пользователей из сессии
    public void removeSession(Long userId) {
        sessions.remove(userId);
    }
}
