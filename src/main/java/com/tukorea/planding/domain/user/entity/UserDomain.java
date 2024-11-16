package com.tukorea.planding.domain.user.entity;

import com.tukorea.planding.domain.group.entity.domain.GroupFavoriteDomain;
import com.tukorea.planding.domain.group.entity.domain.UserGroupDomain;
import com.tukorea.planding.domain.user.dto.AndroidLoginRequest;
import com.tukorea.planding.global.oauth.details.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class UserDomain {
    private final Long id;
    private final String email;
    private final String profileImage;
    private final String username;
    private final Role role;
    private final SocialType socialType;
    private final String socialId;
    private final String userCode;
    private final boolean alarm;
    private final String fcmToken;
    private final Set<UserGroupDomain> userGroup;
    private final List<GroupFavoriteDomain> groupFavorites = new ArrayList<>();

    @Builder
    public UserDomain(Long id, String email, String profileImage, String username, Role role, SocialType socialType, String socialId, String userCode, boolean alarm, String fcmToken, Set<UserGroupDomain> userGroup) {
        this.id = id;
        this.email = email;
        this.profileImage = profileImage;
        this.username = username;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
        this.userCode = userCode;
        this.alarm = alarm;
        this.fcmToken = fcmToken;
        this.userGroup = userGroup;
    }


    public static UserDomain androidCreate(AndroidLoginRequest androidLoginRequest, String userCode) {
        return UserDomain.builder()
                .socialId(androidLoginRequest.socialId())
                .socialType(SocialType.KAKAO)
                .username(androidLoginRequest.profileNickname())
                .email(androidLoginRequest.accountEmail())
                .profileImage(androidLoginRequest.profileImage())
                .userCode(userCode)
                .role(Role.USER)
                .build();
    }

    public UserDomain updateFcmToken(String fcmToken) {
        return UserDomain.builder()
                .id(id)
                .email(email)
                .profileImage(profileImage)
                .username(username)
                .role(role)
                .socialType(socialType)
                .socialId(socialId)
                .alarm(alarm)
                .userCode(userCode)
                .fcmToken(fcmToken)
                .build();
    }

    public UserDomain updateAlarm(boolean alarm) {
        return UserDomain.builder()
                .id(id)
                .email(email)
                .profileImage(profileImage)
                .username(username)
                .role(role)
                .socialType(socialType)
                .socialId(socialId)
                .alarm(alarm)
                .userCode(userCode)
                .fcmToken(fcmToken)
                .build();
    }

    public UserDomain addUserGroup(UserGroupDomain userGroupDomain) {
        Set<UserGroupDomain> updatedUserGroups = userGroup == null ? new HashSet<>() : new HashSet<>(this.userGroup);
        updatedUserGroups.add(userGroupDomain);

        return UserDomain.builder()
                .id(id)
                .email(email)
                .profileImage(profileImage)
                .username(username)
                .role(role)
                .socialType(socialType)
                .socialId(socialId)
                .alarm(alarm)
                .userCode(userCode)
                .fcmToken(fcmToken)
                .userGroup(updatedUserGroups)
                .build();
    }



}
