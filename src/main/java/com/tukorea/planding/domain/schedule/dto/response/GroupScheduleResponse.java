package com.tukorea.planding.domain.schedule.dto.response;

import com.tukorea.planding.domain.planner.dto.GroupPlannerResponse;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleType;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record GroupScheduleResponse(
        Long id,
        String title,
        String content,
        LocalDate scheduleDate,
        Integer startTime,
        Integer endTime,
        DayOfWeek day,
        ScheduleType type,
        String groupName,
        List<UserScheduleAttendance> userScheduleAttendances,
        List<GroupPlannerResponse> planners
) {
    public static GroupScheduleResponse from(Schedule schedule, String groupName, List<UserScheduleAttendance> attendances) {
        return GroupScheduleResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .scheduleDate(schedule.getScheduleDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .day(schedule.getScheduleDate().getDayOfWeek())
                .type(ScheduleType.GROUP)
                .groupName(groupName)
                .userScheduleAttendances(attendances)
                .planners(schedule.getPlanners().stream()
                        .map(GroupPlannerResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
