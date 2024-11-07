package com.tukorea.planding.domain.user.dto;

import com.tukorea.planding.domain.group.service.port.RedisGroupInviteService;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.global.oauth.details.Role;
import lombok.Builder;

@Builder
public record ProfileResponse(
        String username,
        String email,
        String profileImage,
        Role role,
        String userCode,
        Long groupFavorite,
        Long groupRequest
) {
    public static ProfileResponse toProfileResponse(UserDomain userDomain, RedisGroupInviteService redisGroupInviteService){
        return ProfileResponse.builder()
                .userCode(userDomain.getUserCode())
                .email(userDomain.getEmail())
                .username(userDomain.getUsername())
                .profileImage(userDomain.getProfileImage())
                .groupFavorite((long) userDomain.getGroupFavorites().size())
                .groupRequest((long) redisGroupInviteService.getAllInvitations(userDomain.getUserCode()).size())
                .role(Role.USER)
                .build();
    }
}
