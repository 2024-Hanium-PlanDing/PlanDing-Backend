package com.tukorea.planding.domain.user.service;

import com.tukorea.planding.domain.group.service.RedisGroupInviteService;
import com.tukorea.planding.domain.user.dto.AndroidLoginRequest;
import com.tukorea.planding.domain.user.dto.ProfileResponse;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.entity.SocialType;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.global.oauth.details.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserQueryService userQueryService;
    private final RedisGroupInviteService redisGroupInviteService;

    public User createUserFromRequest(AndroidLoginRequest androidLoginRequest) {

        String userCode = generateUniqueUserCode();
        User user = User.builder()
                .socialId(androidLoginRequest.socialId())
                .socialType(SocialType.KAKAO)
                .username(androidLoginRequest.profileNickname())
                .email(androidLoginRequest.accountEmail())
                .profileImage(androidLoginRequest.profileImage())
                .userCode(userCode)
                .role(Role.USER)
                .build();

        User savedUser = userQueryService.save(user);
        return savedUser;
    }

    //TODO 즐겨찾는 그룹, 그룹 요청
    // N+1문제는 발생하지 않지만 user조회쿼리 3개나감
    public ProfileResponse getProfile(UserInfo userInfo) {
        User userProfile = userQueryService.getUserProfile(userInfo.getId());
        return ProfileResponse.builder()
                .userCode(userProfile.getUserCode())
                .email(userProfile.getEmail())
                .username(userProfile.getUsername())
                .profileImage(userProfile.getProfileImage())
                .groupFavorite((long) userProfile.getGroupFavorites().size())
                .groupRequest((long) redisGroupInviteService.getAllInvitations(userInfo.getUserCode()).size())
                .role(Role.USER)
                .build();
    }

    public String generateUniqueUserCode() {
        String userCode;
        do {
            userCode = User.createCode();
        } while (userQueryService.existsByUserCode(userCode));
        return userCode;
    }

    public void updateAlarmSetting(String userCode, boolean alarmSetting) {
        User user = userQueryService.getUserByUserCode(userCode);
        user.updateAlarm(alarmSetting);
    }

    @Transactional(readOnly = true)
    public String getFcmTokenByUserCode(String userCode) {
        User user = userQueryService.getUserByUserCode(userCode);
        if (user != null) {
            return user.getFcmToken();
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public void updateFcmToken(String userCode, String fcmToken) {
        User user = userQueryService.getUserByUserCode(userCode);
        if (user != null) {
            user.updateFcmToken(fcmToken);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
