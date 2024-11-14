package com.tukorea.planding.domain.planner.entity.domain;

import com.tukorea.planding.domain.planner.PlannerRole;
import com.tukorea.planding.domain.planner.PlannerStatus;
import com.tukorea.planding.domain.planner.entity.PlannerUser;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.domain.ScheduleDomain;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PlannerDomain {
    private Long id;

    private Integer plannerNumber;

    private String title;

    private String content;

    private PlannerStatus status;

    private LocalDateTime deadline;

    private ScheduleDomain schedule;

    private final List<PlannerUserDomain> users = new ArrayList<>();


    @Builder
    public PlannerDomain(Long id, Integer plannerNumber, String title, String content, PlannerStatus status, LocalDateTime deadline, ScheduleDomain schedule) {
        this.id = id;
        this.plannerNumber = plannerNumber;
        this.title = title;
        this.content = content;
        this.status = status;
        this.deadline = deadline;
        this.schedule = schedule;
    }

    public PlannerDomain update(String title, String content, PlannerStatus status, LocalDateTime deadline) {
        return PlannerDomain.builder()
                .id(id)
                .content(content)
                .deadline(deadline)
                .title(title)
                .plannerNumber(plannerNumber)
                .schedule(schedule)
                .status(status)
                .build();
    }


    public void generatePlannerNumber() {
        this.plannerNumber = schedule.getPlanners().size() + 1;
    }

    public void declinePlannerNumber() {
        this.plannerNumber = schedule.getPlanners().size() - 1;
    }

    public void updateManager(UserDomain newManager) {
        this.users.stream()
                .filter(plannerUser -> plannerUser.getRole() == PlannerRole.MANAGER)
                .forEach(plannerUser -> plannerUser.updateRole(PlannerRole.GENERAL));

        this.users.stream()
                .filter(plannerUser -> plannerUser.getUser().equals(newManager))
                .findFirst()
                .ifPresentOrElse(
                        plannerUser -> plannerUser.updateRole(PlannerRole.MANAGER),
                        () -> this.users.add(PlannerUserDomain.builder()
                                .planner(this)
                                .user(newManager)
                                .role(PlannerRole.MANAGER)
                                .build())
                );
    }
}
