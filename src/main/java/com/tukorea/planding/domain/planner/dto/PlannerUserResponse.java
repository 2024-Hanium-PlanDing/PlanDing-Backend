package com.tukorea.planding.domain.planner.dto;

import com.tukorea.planding.domain.planner.entity.PlannerUser;
import lombok.Builder;

@Builder
public record PlannerUserResponse(
        String userCode,
        String username,
        String profileImage
) {
    public static PlannerUserResponse fromEntity(PlannerUser plannerUser) {
        return PlannerUserResponse.builder()
                .userCode(plannerUser.getUser().getUserCode())
                .username(plannerUser.getUser().getUsername())
                .profileImage(plannerUser.getUser().getProfileImage())
                .build();
    }
}
