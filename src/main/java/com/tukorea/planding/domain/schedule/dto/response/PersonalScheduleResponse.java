package com.tukorea.planding.domain.schedule.dto.response;

import com.tukorea.planding.domain.planner.dto.GroupPlannerResponse;
import com.tukorea.planding.domain.planner.dto.personal.PersonalPlannerResponse;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleType;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record PersonalScheduleResponse(
        Long id,
        String title,
        String content,
        LocalDate scheduleDate,
        Integer startTime,
        Integer endTime,
        boolean complete,
        ScheduleType type,
        DayOfWeek day,
        List<PersonalPlannerResponse> planners
) {

    public static PersonalScheduleResponse from(Schedule schedule) {
        return PersonalScheduleResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .content(schedule.getContent())
                .scheduleDate(schedule.getScheduleDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .complete(schedule.isComplete())
                .day(schedule.getScheduleDate().getDayOfWeek())
                .type(ScheduleType.PERSONAL)
                .planners(schedule.getPlanners().stream()
                        .map(PersonalPlannerResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }


    public static Comparator<PersonalScheduleResponse> getComparatorByStartTime() {
        return Comparator.comparing(schedule -> schedule.startTime());
    }
}
