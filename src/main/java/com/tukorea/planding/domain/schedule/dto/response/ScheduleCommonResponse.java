package com.tukorea.planding.domain.schedule.dto.response;

import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ScheduleCommonResponse(
        Long id,
        String title,
        String content,
        LocalDate scheduleDate,
        Integer startTime,
        Integer endTime,
        String groupName,
        String groupCode,
        String groupThumbnail
) {
    public static ScheduleCommonResponse from(Schedule schedule) {
        return ScheduleCommonResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .scheduleDate(schedule.getScheduleDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .groupName(schedule.getType() == ScheduleType.GROUP ? schedule.getGroupSchedule().getGroupRoom().getName() : null)
                .groupCode(schedule.getType()==ScheduleType.GROUP ? schedule.getGroupSchedule().getGroupRoom().getGroupCode():null)
                .groupThumbnail(schedule.getType()==ScheduleType.GROUP ? schedule.getGroupSchedule().getGroupRoom().getThumbnail():null)
                .build();
    }
}
