package com.tukorea.planding.domain.planner.dto.personal;

import com.tukorea.planding.domain.planner.PlannerRole;
import com.tukorea.planding.domain.planner.PlannerStatus;
import com.tukorea.planding.domain.planner.entity.Planner;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PersonalPlannerResponse(
        Long id,
        Integer plannerNumber,
        String title,
        String content,
        PlannerStatus status,
        LocalDateTime deadline,
        String managerCode,
        Long scheduleId
) {
    public static PersonalPlannerResponse fromEntity(Planner planner) {
        return PersonalPlannerResponse.builder()
                .id(planner.getId())
                .plannerNumber(planner.getPlannerNumber())
                .title(planner.getTitle())
                .content(planner.getContent())
                .status(planner.getStatus())
                .deadline(planner.getDeadline())
                .managerCode(planner.getUsers().stream()
                        .filter(pu -> pu.getRole() == PlannerRole.MANAGER)
                        .findFirst()
                        .map(pu -> pu.getUser().getUserCode())
                        .orElse(null))
                .scheduleId(planner.getSchedule().getId())
                .build();
    }
}
