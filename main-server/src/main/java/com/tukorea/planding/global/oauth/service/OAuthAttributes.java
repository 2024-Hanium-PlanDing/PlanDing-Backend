package com.tukorea.planding.global.oauth.service;

import com.tukorea.planding.domain.notify.entity.UserNotificationSetting;
import com.tukorea.planding.global.oauth.details.Role;
import com.tukorea.planding.global.oauth.userInfo.KakaoOAuth2UserInfo;

import com.tukorea.planding.global.oauth.userInfo.OAuth2UserInfo;
import com.tukorea.planding.domain.user.entity.SocialType;
import com.tukorea.planding.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private String nameAttributeKey;
    private OAuth2UserInfo oauth2UserInfo;

    @Builder
    private OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    public static OAuthAttributes of(SocialType socialType,
                                     String userNameAttributeName, Map<String, Object> attributes) {
        return ofKakao(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public User toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo, String userCode) {
        return User.builder()
                .socialType(socialType)
                .socialId(oauth2UserInfo.getOAuth2Id())
                .email(oauth2UserInfo.getEmail())
                .username(oauth2UserInfo.getNickname())
                .profileImage(oauth2UserInfo.getProfileImage())
                .role(Role.USER)
                .userCode(userCode)
                .build();
    }

    public UserNotificationSetting toNotificationSetting(User user) {
        return UserNotificationSetting.builder()
                .userCode(user.getUserCode())
                .scheduleNotificationEnabled(true)
                .groupScheduleNotificationEnabled(true)
                .build();
    }
}
