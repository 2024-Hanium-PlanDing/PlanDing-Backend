package com.tukorea.planding.domain.user.dto;

import com.tukorea.planding.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class UserInfoSimple {
    private Long id;
    private String userCode;
    private String userName;
    private String email;
    private String profileImageUrl;
    @Setter
    private boolean hasPermission;

    public static UserInfoSimple fromEntity(User user, String ownerCode) {
        return UserInfoSimple.builder()
                .id(user.getId())
                .userCode(user.getUserCode())
                .userName(user.getUsername())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImage())
                .hasPermission(user.getUserCode().equals(ownerCode))
                .build();
    }

}
