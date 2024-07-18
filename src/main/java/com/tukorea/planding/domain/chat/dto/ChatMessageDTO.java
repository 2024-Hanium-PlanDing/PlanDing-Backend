package com.tukorea.planding.domain.chat.dto;

import lombok.Getter;

@Getter
public class ChatMessageDTO {
    private String groupCode;
    private String content;
    private String sender;
    private MessageType type;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    public void join(ChatMessageDTO messageDTO) {
        this.content = messageDTO.getSender() + "님이 입장 하셨습니다.";
    }
}
