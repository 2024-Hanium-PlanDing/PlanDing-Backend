package com.tukorea.planding.domain.schedule.dto.response;

import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleType;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Comparator;

@Builder
public record ScheduleResponse(
        Long id,
        String title,
        String content,
        LocalDate scheduleDate,
        Integer startTime,
        Integer endTime,
        boolean complete,
        String groupName,
        ScheduleType type
) {

    public static ScheduleResponse from(Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .scheduleDate(schedule.getScheduleDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .complete(schedule.isComplete())
                .type(schedule.getType())
                .groupName(schedule.getType() == ScheduleType.GROUP ? schedule.getGroupSchedule().getGroupRoom().getName() : null)
                .build();
    }


    public static Comparator<ScheduleResponse> getComparatorByStartTime() {
        return Comparator.comparing(ScheduleResponse::startTime);
    }
}
