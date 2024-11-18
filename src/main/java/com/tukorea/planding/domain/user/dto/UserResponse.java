package com.tukorea.planding.domain.user.dto;

import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.global.oauth.details.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserResponse {
    private Long id;

    private String username;

    private String email;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String userCode;

    public static UserResponse toResponse(UserDomain userDomain) {
        return UserResponse.builder()
                .id(userDomain.getId())
                .email(userDomain.getEmail())
                .profileImage(userDomain.getProfileImage())
                .role(userDomain.getRole())
                .username(userDomain.getUsername())
                .userCode(userDomain.getUserCode())
                .build();
    }
}
