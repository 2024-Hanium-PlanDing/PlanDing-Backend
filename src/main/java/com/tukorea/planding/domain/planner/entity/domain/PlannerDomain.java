package com.tukorea.planding.domain.planner.entity.domain;

import com.tukorea.planding.domain.planner.PlannerStatus;
import com.tukorea.planding.domain.planner.entity.PlannerUser;
import com.tukorea.planding.domain.schedule.entity.Schedule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PlannerDomain {
    private Long id;

    private Integer plannerNumber;

    private String title;

    private String content;

    private PlannerStatus status;

    private LocalDateTime deadline;

    private Schedule schedule;

    private final List<PlannerUserDomain> users = new ArrayList<>();

}
