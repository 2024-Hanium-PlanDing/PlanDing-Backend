package com.tukorea.planding.domain.group.entity;

import com.tukorea.planding.domain.group.dto.request.GroupCreateRequest;
import com.tukorea.planding.domain.schedule.entity.GroupSchedule;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.global.audit.BaseEntity;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "owner", nullable = false)
    private String owner; // 그룹룸의 소유자

    @Column(name = "group_code", nullable = false, unique = true)
    private String groupCode; // 그룹방 고유 식별값

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "alarm")
    private boolean alarm = true;

    @OneToMany(mappedBy = "groupRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<UserGroup> userGroups = new HashSet<>();

    @OneToMany(mappedBy = "groupRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<GroupSchedule> groupSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "groupRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<GroupFavorite> groupFavorites = new ArrayList<>();

    @Builder
    public GroupRoom(String name, String description, String owner, String groupCode) {
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
                .owner(owner.getUserCode())
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
