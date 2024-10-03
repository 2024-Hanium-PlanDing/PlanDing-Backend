package com.tukorea.planding.domain.planner.dto.group;

import com.tukorea.planding.domain.notify.entity.NotificationType;
import com.tukorea.planding.domain.planner.dto.PlannerResponse;
import com.tukorea.planding.domain.planner.entity.Planner;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PlannerWeekResponse(
        List<PlannerResponse> planners,
        Long scheduleId,
        String scheduleTitle,
        LocalDate scheduleDate,
        NotificationType type
) {
    public static PlannerWeekResponse fromEntity(Planner planner) {
        return PlannerWeekResponse.builder()
                .planners(List.of(PlannerResponse.fromEntity(planner)))
                .scheduleDate(planner.getSchedule().getScheduleDate())
                .scheduleTitle(planner.getSchedule().getTitle())
                .scheduleId(planner.getSchedule().getId())
                .type(NotificationType.PLANNER)
                .build();
    }
}
