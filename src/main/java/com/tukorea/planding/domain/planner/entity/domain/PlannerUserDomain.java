package com.tukorea.planding.domain.planner.entity.domain;

import com.tukorea.planding.domain.planner.PlannerRole;
import com.tukorea.planding.domain.planner.entity.Planner;
import com.tukorea.planding.domain.user.entity.User;

public class PlannerUserDomain {
    private Long id;

    private PlannerDomain planner;

    private User user;

    private PlannerRole role;

}
