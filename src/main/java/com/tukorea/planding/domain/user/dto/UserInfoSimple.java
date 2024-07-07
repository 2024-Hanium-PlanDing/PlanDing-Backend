package com.tukorea.planding.domain.user.dto;

import com.tukorea.planding.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserInfoSimple {
    private Long id;
    private String userName;
    private String email;
    private String profileImageUrl;

    public static UserInfoSimple fromEntity(User user) {
        return UserInfoSimple.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImage())
                .build();
    }
}
