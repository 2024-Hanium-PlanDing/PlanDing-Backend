package com.tukorea.planding.domain.group.dto.response;

import com.tukorea.planding.domain.group.entity.domain.GroupRoomDomain;
import com.tukorea.planding.domain.user.dto.UserInfoSimple;
import com.tukorea.planding.domain.user.dto.UserResponse;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record GroupInformationResponse(
        Long id,
        String owner,
        String name,
        String groupCode,
        String description,
        String thumbnailUrl,
        LocalDate createdBy,
        boolean isFavorite,
        boolean isAlarm,
        List<UserInfoSimple> users,
        boolean isGroupAdmin
) {

    public static GroupInformationResponse from(GroupRoomDomain groupRoomDomain, List<UserInfoSimple> userInfoSimples, UserResponse userResponse) {
        return GroupInformationResponse.builder()
                .id(groupRoomDomain.getId())
                .owner(groupRoomDomain.getOwner().getUserCode())
                .users(userInfoSimples)
                .groupCode(groupRoomDomain.getGroupCode())
                .name(groupRoomDomain.getName())
                .description(groupRoomDomain.getDescription())
                .createdBy(LocalDate.from(groupRoomDomain.getCreatedDate()))
                .thumbnailUrl(groupRoomDomain.getThumbnail())
                .isGroupAdmin(groupRoomDomain.isGroupOwner(userResponse))
                .isFavorite(groupRoomDomain.getGroupFavorites().stream()
                        .anyMatch(a -> a.getGroupRoomDomain().getGroupCode().equals(groupRoomDomain.getGroupCode())))
                .isAlarm(groupRoomDomain.isAlarm())
                .build();
    }


}
