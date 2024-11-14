package com.tukorea.planding.domain.planner.entity.domain;

import com.tukorea.planding.domain.planner.PlannerRole;
import com.tukorea.planding.domain.planner.entity.Planner;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PlannerUserDomain {
    private final Long id;
    private final PlannerDomain planner;
    private final UserDomain user;
    private final PlannerRole role;

    @Builder
    public PlannerUserDomain(Long id, PlannerDomain planner, UserDomain user, PlannerRole role) {
        this.id = id;
        this.planner = planner;
        this.user = user;
        this.role = role;
    }

    public PlannerUserDomain updateRole(PlannerRole newRole) {
        return PlannerUserDomain.builder()
                .id(id)
                .planner(planner)
                .user(user)
                .role(newRole)
                .build();
    }

}
