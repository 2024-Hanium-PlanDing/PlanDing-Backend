package com.tukorea.planding.domain.planner.dto;

import com.tukorea.planding.domain.planner.PlannerRole;
import com.tukorea.planding.domain.planner.PlannerStatus;
import com.tukorea.planding.domain.planner.entity.Planner;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record PlannerResponse(
        Long id,
        Long scheduleId,
        Integer plannerNumber,
        String title,
        String content,
        PlannerStatus status,
        LocalDateTime deadline,
        PlannerUserResponse manager,
        List<PlannerUserResponse> users
) {
    public static PlannerResponse fromEntity(Planner planner) {
        return PlannerResponse.builder().id(planner.getId())
                .scheduleId(planner.getSchedule().getId())
                .plannerNumber(planner.getPlannerNumber())
                .title(planner.getTitle())
                .content(planner.getContent())
                .status(planner.getStatus())
                .deadline(planner.getDeadline())
                .manager(planner.getUsers().stream()
                        .filter(pu -> pu.getRole() == PlannerRole.MANAGER)
                        .findFirst()
                        .map(PlannerUserResponse::fromEntity)
                        .orElse(null))
                .users(planner.getUsers().stream()
                        .filter(pu -> pu.getRole() == PlannerRole.GENERAL)
                        .map(PlannerUserResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
