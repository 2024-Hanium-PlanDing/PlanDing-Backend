package com.tukorea.planding.domain.planner.dto;

import com.tukorea.planding.domain.notify.entity.NotificationType;
import com.tukorea.planding.domain.planner.entity.Planner;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleType;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record SchedulePlannerResponse(
        List<PlannerResponse> planners,
        Long scheduleId,
        NotificationType type,
        ScheduleType scheduleType
) {
    public static SchedulePlannerResponse fromEntity(List<Planner> planners, Schedule schedule) {
        List<PlannerResponse> plannerResponses = planners.stream()
                .map(PlannerResponse::fromEntity)
                .collect(Collectors.toList());

        return SchedulePlannerResponse.builder()
                .planners(plannerResponses)
                .scheduleId(schedule.getId())
                .type(NotificationType.PLANNER)
                .scheduleType(schedule.getType())
                .build();
    }
}
