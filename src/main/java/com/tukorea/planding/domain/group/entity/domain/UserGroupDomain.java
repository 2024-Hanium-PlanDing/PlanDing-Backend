package com.tukorea.planding.domain.group.entity.domain;


import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.entity.UserGroup;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserGroupDomain {
    private final Long id;
    private final UserDomain user;
    private final GroupRoomDomain groupRoom;
    private final boolean isConnected;

    @Builder
    public UserGroupDomain(Long id, UserDomain user, GroupRoomDomain groupRoom, boolean isConnected) {
        this.id = id;
        this.user = user;
        this.groupRoom = groupRoom;
        this.isConnected = isConnected;
    }

    public static UserGroupDomain createUserGroup(UserDomain userDomain, GroupRoomDomain groupRoomDomain) {
        return UserGroupDomain.builder()
                .user(userDomain)
                .groupRoom(groupRoomDomain)
                .build();
    }

    public static UserGroupDomain create(UserDomain userDomain, GroupRoomDomain groupRoomDomain) {
        return UserGroupDomain.builder()
                .user(userDomain)
                .groupRoom(groupRoomDomain)
                .build();
    }

    public UserGroupDomain updateConnect(boolean isConnected) {
        return UserGroupDomain.builder()
                .user(user)
                .groupRoom(groupRoom)
                .isConnected(isConnected)
                .build();
    }
}
