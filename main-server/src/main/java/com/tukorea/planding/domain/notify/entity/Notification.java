package com.tukorea.planding.domain.notify.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userCode;

    @Column(name = "message")
    private String message;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "url")
    private String url;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(name = "created_at")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @Column(name = "schedule_time")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime scheduleTime;

    @Builder
    public Notification(String userCode, String message, String groupName, String url, NotificationType notificationType, LocalDateTime createdAt, LocalDateTime scheduleTime) {
        this.userCode = userCode;
        this.message = message;
        this.groupName = groupName;
        this.url = url;
        this.notificationType = notificationType;
        this.createdAt = createdAt;
        this.scheduleTime = scheduleTime;
    }
}
