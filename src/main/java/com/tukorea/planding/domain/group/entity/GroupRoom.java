package com.tukorea.planding.domain.group.entity;

import com.tukorea.planding.domain.group.entity.domain.GroupRoomDomain;
import com.tukorea.planding.domain.schedule.entity.GroupSchedule;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.*;
import java.util.stream.Collectors;

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
    public GroupRoom(Long id, String name, String description, User owner, String groupCode, String thumbnail, boolean alarm) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.groupCode = groupCode;
        this.thumbnail = thumbnail;
        this.alarm = alarm;
    }

    public GroupRoomDomain toModel() {
        return GroupRoomDomain.builder()
                .id(id)
                .createdDate(getCreatedDate())
                .modifiedDate(getModifiedDate())
                .groupCode(groupCode)
                .owner(owner.toModel())
                .thumbnail(thumbnail)
                .description(description)
                .name(name)
                .alarm(alarm)
                .userGroups(userGroups.stream().map(UserGroup::toModel).collect(Collectors.toSet()))
                .groupFavorites(groupFavorites.stream().map(GroupFavorite::toModel).collect(Collectors.toList()))
                .build();
    }

    public static GroupRoom fromModel(GroupRoomDomain groupRoomDomain) {
        return GroupRoom.builder()
                .id(groupRoomDomain.getId())
                .groupCode(groupRoomDomain.getGroupCode())
                .owner(User.fromModel(groupRoomDomain.getOwner()))
                .thumbnail(groupRoomDomain.getThumbnail())
                .description(groupRoomDomain.getDescription())
                .name(groupRoomDomain.getName())
                .alarm(groupRoomDomain.isAlarm())
                .build();
    }


}
