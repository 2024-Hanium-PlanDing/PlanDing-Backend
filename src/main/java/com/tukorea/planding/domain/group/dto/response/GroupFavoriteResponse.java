package com.tukorea.planding.domain.group.dto.response;

import com.tukorea.planding.domain.group.entity.domain.GroupFavoriteDomain;
import lombok.Builder;

@Builder
public record GroupFavoriteResponse(
        String groupName,
        String groupCode
) {
    public static GroupFavoriteResponse from(GroupFavoriteDomain groupFavoriteDomain) {
        return GroupFavoriteResponse.builder()
                .groupName(groupFavoriteDomain.getGroupRoomDomain().getName())
                .groupCode(groupFavoriteDomain.getGroupRoomDomain().getGroupCode())
                .build();
    }
}
