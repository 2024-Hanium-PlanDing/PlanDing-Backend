package com.tukorea.planding.domain.schedule.dto.response;

import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SimpleGroupScheduleResponse(
        Long id,
        String title,
        LocalDate scheduleDate,
        Integer startTime,
        Integer endTime,
        ScheduleType type
) {
    public static SimpleGroupScheduleResponse from(Schedule schedule) {
        return SimpleGroupScheduleResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .scheduleDate(schedule.getScheduleDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .type(ScheduleType.GROUP)
                .build();
    }
}
