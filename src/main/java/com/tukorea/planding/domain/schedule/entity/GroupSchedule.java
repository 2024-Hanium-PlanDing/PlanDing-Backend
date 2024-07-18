package com.tukorea.planding.domain.schedule.entity;

import com.tukorea.planding.domain.group.entity.GroupRoom;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "GROUP_SCHEDULE")
public class GroupSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_room_id")
    private GroupRoom groupRoom;

    @OneToMany(mappedBy = "groupSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Schedule> schedules = new HashSet<>();

    @Builder
    public GroupSchedule(GroupRoom groupRoom) {
        this.groupRoom = groupRoom;
    }
}
