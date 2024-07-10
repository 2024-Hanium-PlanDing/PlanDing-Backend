package com.tukorea.planding.domain.user.service;

import com.tukorea.planding.domain.group.entity.GroupFavorite;
import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.service.RedisGroupInviteService;
import com.tukorea.planding.domain.notify.entity.UserNotificationSetting;
import com.tukorea.planding.domain.notify.repository.setting.UserNotificationSettingRepository;
import com.tukorea.planding.domain.user.dto.AndroidLoginRequest;
import com.tukorea.planding.domain.user.dto.ProfileResponse;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.entity.SocialType;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.global.oauth.details.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserQueryService userQueryService;
    private final RedisGroupInviteService redisGroupInviteService;
    private final UserNotificationSettingRepository userNotificationSettingRepository;


    @Transactional(readOnly = true)
    public List<GroupRoom> findFavoriteGroupsByUserId(UserInfo userInfo) {
        return userQueryService.getUserByUserCode(userInfo.getUserCode())
                .getGroupFavorites()
                .stream()
                .map(GroupFavorite::getGroupRoom)
                .collect(Collectors.toList());
    }

    @Transactional
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

        UserNotificationSetting defaultSetting = UserNotificationSetting.builder()
                .userCode(userCode)
                .scheduleNotificationEnabled(true)
                .groupScheduleNotificationEnabled(true)
                .build();

        userNotificationSettingRepository.save(defaultSetting);

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
}
