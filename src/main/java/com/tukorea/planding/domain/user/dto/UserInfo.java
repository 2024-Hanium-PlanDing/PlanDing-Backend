package com.tukorea.planding.domain.user.dto;

import com.tukorea.planding.global.oauth.details.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {
    private Long id;

    private String username;

    private String email;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String userCode;
}
