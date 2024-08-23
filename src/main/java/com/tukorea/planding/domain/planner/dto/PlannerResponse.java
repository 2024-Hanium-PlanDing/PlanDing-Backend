package com.tukorea.planding.domain.planner.dto;

import com.tukorea.planding.domain.planner.PlannerRole;
import com.tukorea.planding.domain.planner.PlannerStatus;
import com.tukorea.planding.domain.planner.entity.Planner;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PlannerResponse(
        Long id,
        Integer plannerNumber,
        String title,
        String content,
        PlannerStatus status,
        LocalDateTime deadline,
        String managerCode,
        List<String> userCodes
) {
    public static PlannerResponse fromEntity(Planner planner) {
        return PlannerResponse.builder().id(planner.getId())
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
                .userCodes(planner.getUsers().stream()
                        .filter(pu -> pu.getRole() == PlannerRole.GENERAL)
                        .findFirst()
                        .map(pu -> pu.getUser().getUserCode())
                        .stream().toList())
                .build();
    }
}
