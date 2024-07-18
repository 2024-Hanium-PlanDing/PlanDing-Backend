package com.tukorea.planding.domain.user.mapper;

import com.tukorea.planding.domain.user.dto.AndroidLoginResponse;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserInfo toUserInfo(User user) {
        return UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .profileImage(user.getProfileImage())
                .role(user.getRole())
                .username(user.getUsername())
                .userCode(user.getUserCode())
                .build();
    }

    public static AndroidLoginResponse toAndroidLoginResponse(User user, String accessToken, String refreshToken) {
        return AndroidLoginResponse.builder()
                .userCode(user.getUserCode())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
