package com.tukorea.planding.domain.planner.entity;

import com.tukorea.planding.domain.planner.PlannerStatus;
import com.tukorea.planding.domain.schedule.entity.Schedule;
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
public class Planner {

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

    public void generatePlannerNumber() {
        this.plannerNumber = schedule.getPlanners().size() + 1;
    }

    public void declinePlannerNumber() {
        this.plannerNumber = schedule.getPlanners().size() - 1;
    }

    public void update(String title, String content, PlannerStatus status, LocalDateTime deadline) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.deadline = deadline;
    }

    public void updateManager(){

    }
}
