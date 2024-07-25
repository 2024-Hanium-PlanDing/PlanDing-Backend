package com.tukorea.planding.domain.chat.repository;

import com.tukorea.planding.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByCreatedAtBefore(LocalDateTime dateTime);
    void deleteByCreatedAtBefore(LocalDateTime dateTime);
}
