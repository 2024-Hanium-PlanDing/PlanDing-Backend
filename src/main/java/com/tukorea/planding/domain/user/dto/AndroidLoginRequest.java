package com.tukorea.planding.domain.user.dto;

import com.tukorea.planding.domain.user.entity.SocialType;

public record AndroidLoginRequest(
        String profileNickname,
        String accountEmail,
        String profileImage,
        String socialId
) {
}
