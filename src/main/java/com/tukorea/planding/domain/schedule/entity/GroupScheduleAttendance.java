package com.tukorea.planding.domain.schedule.entity;

import com.tukorea.planding.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class GroupScheduleAttendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ScheduleStatus status = ScheduleStatus.UNDECIDED;

    /*
   스케줄 상태변환 메서드
    */
    public void markAsPossible() {
        this.status = ScheduleStatus.POSSIBLE;
    }

    public void markAsImpossible() {
        this.status = ScheduleStatus.IMPOSSIBLE;
    }

    public void markAsUndecided() {
        this.status = ScheduleStatus.UNDECIDED;
    }

    public void addUser(User user) {
        this.user = user;
    }

    public void addSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
