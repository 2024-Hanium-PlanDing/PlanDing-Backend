package com.tukorea.planding.domain.user.dto;

import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
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

    public static UserInfoSimple fromEntity(UserDomain userDomain, String ownerCode) {
        return UserInfoSimple.builder()
                .id(userDomain.getId())
                .userCode(userDomain.getUserCode())
                .userName(userDomain.getUsername())
                .email(userDomain.getEmail())
                .profileImageUrl(userDomain.getProfileImage())
                .hasPermission(userDomain.getUserCode().equals(ownerCode))
                .build();
    }

}
