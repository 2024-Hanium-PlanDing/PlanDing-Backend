package com.tukorea.planding.domain.user.dto;

import com.tukorea.planding.global.oauth.details.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
}
