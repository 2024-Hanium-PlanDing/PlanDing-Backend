package com.tukorea.planding.domain.notify.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.tukorea.planding.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userCode;

    @Column(name = "scheduleCount")
    private String message;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "url")
    private String url;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(name = "schedule_Date")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDate scheduleDate;

    @Builder
    public Notification(String userCode, String message, String groupName, String url, NotificationType notificationType, LocalDate scheduleDate) {
        this.userCode = userCode;
        this.message = message;
        this.groupName = groupName;
        this.url = url;
        this.notificationType = notificationType;
        this.scheduleDate = scheduleDate;
    }
}
