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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userCode;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
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

    private boolean isRead = false;

    @Builder
    public Notification(String userCode,String title, String message, String groupName, String url, NotificationType notificationType, LocalDate scheduleDate, boolean isRead) {
        this.userCode = userCode;
        this.title=title;
        this.message = message;
        this.groupName = groupName;
        this.url = url;
        this.notificationType = notificationType;
        this.scheduleDate = scheduleDate;
        this.isRead = isRead;
    }

    public void updateRead() {
        this.isRead = true;
    }
}
