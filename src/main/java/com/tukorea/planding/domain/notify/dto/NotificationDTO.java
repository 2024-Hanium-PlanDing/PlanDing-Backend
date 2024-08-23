package com.tukorea.planding.domain.notify.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.tukorea.planding.domain.notify.entity.NotificationType;
import com.tukorea.planding.domain.schedule.dto.request.PersonalScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.request.websocket.SendCreateScheduleDTO;
import com.tukorea.planding.domain.schedule.dto.response.GroupScheduleAttendanceResponse;
import com.tukorea.planding.domain.schedule.dto.response.GroupScheduleResponse;
import com.tukorea.planding.domain.schedule.dto.response.PersonalScheduleResponse;
import com.tukorea.planding.domain.schedule.dto.response.ScheduleResponse;
import com.tukorea.planding.domain.schedule.entity.Schedule;
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
    private String groupCode;

    public static NotificationDTO createPersonalSchedule(String receiverCode, PersonalScheduleResponse response) {
        return NotificationDTO.builder()
                .scheduleId(response.id())
                .userCode(receiverCode)
                .message(response.content())
                .url("/api/v1/schedule/" + response.id())
                .notificationType(NotificationType.PERSONAL_SCHEDULE)
                .date(String.valueOf(response.scheduleDate()))
                .time(response.startTime())
                .build();
    }


    public static NotificationDTO createDailySchedule(String receiverCode, String message, Long scheduleId, String date, Integer time) {
        return NotificationDTO.builder()
                .scheduleId(scheduleId)
                .userCode(receiverCode)
                .message(message)
                .url("/api/v1/schedule/" + scheduleId)
                .notificationType(NotificationType.DAILY)
                .date(date)
                .time(time)
                .build();
    }

    // 그룹 스케줄 생성 시 필요한 빌더 메서드
    public static NotificationDTO createGroupSchedule(String receiverCode, Schedule response) {
        return NotificationDTO.builder()
                .scheduleId(response.getId())
                .userCode(receiverCode)
                .message(response.getContent())
                .url("/api/v1/schedule/" + response.getId())
                .notificationType(NotificationType.GROUP_SCHEDULE)
                .date(String.valueOf(response.getScheduleDate()))
                .time(response.getStartTime())
                .groupName(response.getGroupSchedule().getGroupRoom().getName())
                .groupCode(response.getGroupSchedule().getGroupRoom().getGroupCode())
                .build();
    }


}
