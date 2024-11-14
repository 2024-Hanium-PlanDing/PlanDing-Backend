package com.tukorea.planding.domain.group.entity.domain;

import com.tukorea.planding.domain.user.entity.UserDomain;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GroupFavoriteDomain {

    private final Long id;
    private final UserDomain userDomain;
    private final GroupRoomDomain groupRoomDomain;

    @Builder
    public GroupFavoriteDomain(Long id, UserDomain userDomain, GroupRoomDomain groupRoomDomain) {
        this.id = id;
        this.userDomain = userDomain;
        this.groupRoomDomain = groupRoomDomain;
    }

    public static GroupFavoriteDomain createGroupFavorite(UserDomain userDomain, GroupRoomDomain groupRoomDomain) {
        return GroupFavoriteDomain.builder()
                .userDomain(userDomain)
                .groupRoomDomain(groupRoomDomain)
                .build();
    }
}
