package com.tukorea.planding.domain.planner.dto;

import com.tukorea.planding.domain.notify.entity.NotificationType;
import com.tukorea.planding.domain.planner.entity.Planner;
import com.tukorea.planding.domain.schedule.entity.Action;
import lombok.Builder;

@Builder
public record GroupPlannerResponse(
        PlannerResponse planner,
        Long scheduleId,
        NotificationType type,
        Action action
) {

    public static GroupPlannerResponse fromEntity(Planner planner) {
        return GroupPlannerResponse.builder()
                .planner(PlannerResponse.fromEntity(planner))
                .scheduleId(planner.getSchedule().getId())
                .type(NotificationType.PLANNER)
                .build();
    }

    public static GroupPlannerResponse fromEntity(Planner planner, Action action) {
        return GroupPlannerResponse.builder()
                .planner(PlannerResponse.fromEntity(planner))
                .scheduleId(planner.getSchedule().getId())
                .type(NotificationType.PLANNER)
                .action(action)
                .build();
    }

    public static GroupPlannerResponse delete(Planner planner) {
        return GroupPlannerResponse.builder()
                .scheduleId(planner.getSchedule().getId())
                .type(NotificationType.PLANNER)
                .action(Action.DELETE)
                .build();
    }
}
