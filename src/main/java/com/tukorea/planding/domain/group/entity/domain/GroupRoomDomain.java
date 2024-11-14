package com.tukorea.planding.domain.group.entity.domain;

import com.tukorea.planding.domain.group.dto.request.GroupCreateRequest;
import com.tukorea.planding.domain.group.dto.request.GroupUpdateRequest;
import com.tukorea.planding.domain.schedule.entity.domain.GroupScheduleDomain;
import com.tukorea.planding.domain.user.dto.UserResponse;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
public class GroupRoomDomain {
    private final Long id;
    private final String name;
    private final String description;
    private final UserDomain owner;
    private final String groupCode; // 그룹방 고유 식별값
    private final String thumbnail;
    private final boolean alarm;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;
    private final Set<UserGroupDomain> userGroups;
    private final List<GroupScheduleDomain> groupSchedules;
    private final List<GroupFavoriteDomain> groupFavorites;

    @Builder
    public GroupRoomDomain(Long id, String name, String description, UserDomain owner, String groupCode, String thumbnail, boolean alarm, LocalDateTime createdDate, LocalDateTime modifiedDate, Set<UserGroupDomain> userGroups, List<GroupScheduleDomain> groupSchedules, List<GroupFavoriteDomain> groupFavorites) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.groupCode = groupCode;
        this.thumbnail = thumbnail;
        this.alarm = alarm;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.userGroups = userGroups;
        this.groupSchedules = groupSchedules;
        this.groupFavorites = groupFavorites;
    }

    public static GroupRoomDomain createGroupRoom(GroupCreateRequest groupCreateRequest, String groupCode, User owner) {
        return GroupRoomDomain.builder()
                .name(groupCreateRequest.name())
                .description(groupCreateRequest.description())
                .owner(owner.toModel())
                .groupCode(groupCode)
                .build();
    }

    public GroupRoomDomain update(GroupUpdateRequest request) {
        return GroupRoomDomain.builder()
                .id(id)
                .name(request.name())
                .description(request.description())
                .owner(owner)
                .thumbnail(thumbnail)
                .groupCode(groupCode)
                .createdDate(createdDate)
                .modifiedDate(LocalDateTime.now())
                .alarm(alarm)
                .build();
    }

    public GroupRoomDomain updateAlarm(boolean alarm) {
        return GroupRoomDomain.builder()
                .id(id)
                .name(name)
                .description(description)
                .owner(owner)
                .thumbnail(thumbnail)
                .groupCode(groupCode)
                .createdDate(createdDate)
                .modifiedDate(LocalDateTime.now())
                .alarm(alarm)
                .build();
    }

    public GroupRoomDomain updateThumbnail(String thumbnail) {
        return GroupRoomDomain.builder()
                .id(id)
                .name(name)
                .description(description)
                .owner(owner)
                .thumbnail(thumbnail)
                .groupCode(groupCode)
                .createdDate(createdDate)
                .modifiedDate(LocalDateTime.now())
                .alarm(alarm)
                .build();
    }

    public boolean isGroupOwner(UserResponse userResponse) {
        return this.owner.getUserCode().equals(userResponse.getUserCode());
    }


    public void validateOwner(UserResponse userResponse) {
        if (!this.owner.getUserCode().equals(userResponse.getUserCode())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
    }

    public void hasMember(UserResponse userResponse) {
        if (this.getUserGroups().stream()
                .noneMatch(userGroup -> userGroup.getUser().getUserCode().equals(userResponse.getUserCode()))) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
    }

}
