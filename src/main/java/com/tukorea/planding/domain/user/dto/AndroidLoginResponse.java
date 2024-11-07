package com.tukorea.planding.domain.user.dto;

import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.global.oauth.details.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AndroidLoginResponse {
    private String userCode;
    private String accessToken;
    private String refreshToken;

    public static AndroidLoginResponse toAndroidLoginResponse(UserDomain userDomain, String accessToken, String refreshToken) {
        return AndroidLoginResponse.builder()
                .userCode(userDomain.getUserCode())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
