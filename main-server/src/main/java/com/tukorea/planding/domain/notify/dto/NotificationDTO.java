package com.tukorea.planding.domain.notify.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.tukorea.planding.domain.notify.entity.NotificationType;
import com.tukorea.planding.domain.schedule.dto.request.PersonalScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.response.PersonalScheduleResponse;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NotificationDTO {

    // 식별 ID
    private Long scheduleId;
    private String userCode;
    private String message;
    private String url;
    private NotificationType notificationType;
    private Integer time;
    private String date;
    // 그룹 스케줄에만 해당하는 필드
    private String groupName;

    public static NotificationDTO createPersonalSchedule(Long scheduleId, String receiverCode, PersonalScheduleResponse response) {
        return NotificationDTO.builder()
                .scheduleId(scheduleId)
                .userCode(receiverCode)
                .message(response.content())
                .url("/api/v1/schedule/" + scheduleId)
                .notificationType(NotificationType.PERSONAL_SCHEDULE)
                .date(String.valueOf(response.scheduleDate()))
                .time(response.startTime())
                .build();
    }


    public static NotificationDTO createDailySchedule(String receiverCode, String message, Long scheduleId, String date, Integer time) {
        return NotificationDTO.builder()
                .userCode(receiverCode)
                .message(message)
                .url("/api/v1/schedule/" + scheduleId)
                .notificationType(NotificationType.DAILY)
                .date(date)
                .time(time)
                .build();
    }

    // 그룹 스케줄 생성 시 필요한 빌더 메서드
    public static NotificationDTO createGroupSchedule(String receiverCode, String message, String url, Integer time, String groupName) {
        return NotificationDTO.builder()
                .userCode(receiverCode)
                .message(message)
                .url(url)
                .notificationType(NotificationType.GROUP_SCHEDULE)
                .time(time)
                .groupName(groupName)
                .build();
    }

}
