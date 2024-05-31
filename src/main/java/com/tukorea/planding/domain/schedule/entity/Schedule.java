package com.tukorea.planding.domain.schedule.entity;

import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.schedule.dto.request.ScheduleRequest;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.global.audit.BaseEntity;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "SCHEDULE")
public class Schedule extends BaseEntity {

    @Id
    @Column(name = "schedule_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "schedule_date", nullable = false)
    private LocalDate scheduleDate;

    @Column(name = "start_time", nullable = false)
    private Integer startTime;

    @Column(name = "end_time", nullable = false)
    private Integer endTime;

    @Column(name = "complete", nullable = false)
    private boolean isComplete;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ScheduleType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_schedule_id")
    private PersonalSchedule personalSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_schedule_id")
    private GroupSchedule groupSchedule;

    @Builder
    public Schedule(String title, String content, LocalDate scheduleDate, Integer startTime, Integer endTime, boolean isComplete, ScheduleType type, PersonalSchedule personalSchedule, GroupSchedule groupSchedule) {
        this.title = title;
        this.content = content;
        this.scheduleDate = scheduleDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isComplete = isComplete;
        this.type = type;
        this.personalSchedule = personalSchedule;
        this.groupSchedule = groupSchedule;
    }

    public void update(String title, String content, Integer startTime, Integer endTime) {
        Optional.ofNullable(title).ifPresent(value -> this.title = value);
        Optional.ofNullable(content).ifPresent(value -> this.content = value);

        if (startTime != null && endTime != null && startTime > endTime) {
            throw new IllegalArgumentException("startTime은 endTime보다 빨라야 합니다.");
        }
        Optional.ofNullable(startTime).ifPresent(value -> this.startTime = value);
        Optional.ofNullable(endTime).ifPresent(value -> this.endTime = value);
    }
}
