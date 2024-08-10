package com.tukorea.planding.domain.notify.dto;

import com.tukorea.planding.domain.notify.entity.Notification;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationScheduleResponse {
    private Long id;
    private String message;
    private String groupName;
    private String url;
    private LocalDateTime createdAt;

    public static NotificationScheduleResponse of(Notification notification) {
        return NotificationScheduleResponse.builder()
                .id(notification.getId())
                .groupName(notification.getGroupName())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedDate())
                .url(notification.getUrl())
                .build();
    }
}
