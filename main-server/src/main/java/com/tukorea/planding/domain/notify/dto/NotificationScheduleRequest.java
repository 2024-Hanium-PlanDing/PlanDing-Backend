package com.tukorea.planding.domain.notify.dto;

import com.tukorea.planding.domain.notify.entity.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationScheduleRequest {
    private NotificationType type;
    private LocalDateTime createdAt;
    private String groupName;
    private String message;
    private String receiverCode;
    private String groupCode;
    private String url;
}
