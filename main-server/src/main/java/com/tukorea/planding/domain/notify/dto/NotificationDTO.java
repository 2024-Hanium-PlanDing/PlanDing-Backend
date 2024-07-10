package com.tukorea.planding.domain.notify.dto;

import com.tukorea.planding.domain.notify.entity.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    private String userCode;
    private String message;
    private String url;
    private NotificationType notificationType;
    private String createdAt;
    // 그룹 스케줄에만 해당하는 필드
    private String groupName;

    public static NotificationDTO createPersonalSchedule(String receiverCode, String message, String url, String createdAt) {
        return NotificationDTO.builder()
                .userCode(receiverCode)
                .message(message)
                .url(url)
                .notificationType(NotificationType.PERSONAL_SCHEDULE)
                .createdAt(createdAt)
                .build();
    }

    // 그룹 스케줄 생성 시 필요한 빌더 메서드
    public static NotificationDTO createGroupSchedule(String receiverCode, String message, String url, String createdAt, String groupName) {
        return NotificationDTO.builder()
                .userCode(receiverCode)
                .message(message)
                .url(url)
                .notificationType(NotificationType.GROUP_SCHEDULE)
                .createdAt(createdAt)
                .groupName(groupName)
                .build();
    }

}
