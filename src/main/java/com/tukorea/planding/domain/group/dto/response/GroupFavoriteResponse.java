package com.tukorea.planding.domain.group.dto.response;

import com.tukorea.planding.domain.group.entity.GroupFavorite;
import lombok.Builder;

@Builder
public record GroupFavoriteResponse(
        String groupName,
        String groupCode
) {
    public static GroupFavoriteResponse from(GroupFavorite groupFavorite) {
        return GroupFavoriteResponse.builder()
                .groupName(groupFavorite.getGroupRoom().getName())
                .groupCode(groupFavorite.getGroupRoom().getGroupCode())
                .build();
    }
}
