package com.tukorea.planding.domain.planner.entity;

import com.tukorea.planding.domain.planner.PlannerRole;
import com.tukorea.planding.domain.planner.PlannerStatus;
import com.tukorea.planding.domain.planner.entity.domain.PlannerDomain;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Planner extends BaseEntity {

    @Id
    @Column(name = "planner_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "planner_number")
    private Integer plannerNumber;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PlannerStatus status;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @OneToMany(mappedBy = "planner", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PlannerUser> users = new ArrayList<>();


    public static Planner fromModel(PlannerDomain plannerDomain) {
        return Planner.builder()
                .id(plannerDomain.getId())
                .content(plannerDomain.getContent())
                .deadline(plannerDomain.getDeadline())
                .title(plannerDomain.getTitle())
                .plannerNumber(plannerDomain.getPlannerNumber())
                .schedule(Schedule.fromModel(plannerDomain.getSchedule()))
                .status(plannerDomain.getStatus())
                .build();
    }

    public PlannerDomain toModel() {
        return PlannerDomain.builder()
                .id(id)
                .content(content)
                .deadline(deadline)
                .title(title)
                .plannerNumber(plannerNumber)
                .schedule(schedule.toModel())
                .status(status)
                .build();
    }
}
