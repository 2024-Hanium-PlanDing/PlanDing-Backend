package com.tukorea.planding.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {

    KAKAO("KAKAO");

    private final String socialName;

    public static SocialType getSocialType(String registrationId) {
        if (KAKAO.socialName.equals(registrationId.toUpperCase())) {
            return SocialType.KAKAO;
        }
        return null;
    }
}
