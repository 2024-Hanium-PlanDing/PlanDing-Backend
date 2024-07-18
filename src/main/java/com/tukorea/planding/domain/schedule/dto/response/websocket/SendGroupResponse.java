package com.tukorea.planding.domain.schedule.dto.response.websocket;

import com.tukorea.planding.domain.schedule.dto.response.ScheduleResponse;
import com.tukorea.planding.domain.schedule.entity.Action;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SendGroupResponse(
        Long id,
        String title,
        String content,
        LocalDate scheduleDate,
        Integer startTime,
        Integer endTime,
        String groupName,
        ScheduleType type,
        Action action
) {
    public static SendGroupResponse from(Schedule schedule, Action action) {
        return SendGroupResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .scheduleDate(schedule.getScheduleDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .type(ScheduleType.GROUP)
                .groupName(schedule.getGroupSchedule().getGroupRoom().getName())
                .action(action)
                .build();
    }

    public static SendGroupResponse delete(Long scheduleId, Action action) {
        return SendGroupResponse.builder()
                .id(scheduleId)
                .action(action)
                .build();
    }
}
