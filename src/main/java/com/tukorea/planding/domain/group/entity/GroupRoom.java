package com.tukorea.planding.domain.group.entity;

import com.tukorea.planding.domain.group.dto.request.GroupCreateRequest;
import com.tukorea.planding.domain.schedule.entity.GroupSchedule;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupRoom extends BaseEntity {

    @Id
    @Column(name = "group_room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(name = "group_code", nullable = false, unique = true)
    private String groupCode; // 그룹방 고유 식별값

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "alarm")
    private boolean alarm = true;

    @BatchSize(size = 11)
    @OneToMany(mappedBy = "groupRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<UserGroup> userGroups = new HashSet<>();

    @OneToMany(mappedBy = "groupRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<GroupSchedule> groupSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "groupRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<GroupFavorite> groupFavorites = new ArrayList<>();

    @Builder
    public GroupRoom(String name, String description, User owner, String groupCode) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.groupCode = groupCode;
    }

    @PrePersist
    public void generateRoomCode() {
        this.groupCode = "G" + UUID.randomUUID().toString();
    }

    public static GroupRoom createGroupRoom(GroupCreateRequest groupCreateRequest, User owner) {
        return GroupRoom.builder()
                .name(groupCreateRequest.name())
                .description(groupCreateRequest.description())
                .owner(owner)
                .build();
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void updateAlarm(boolean alarm) {
        this.alarm = alarm;
    }
}
