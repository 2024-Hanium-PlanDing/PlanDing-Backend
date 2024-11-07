package com.tukorea.planding.domain.user.service;

import com.tukorea.planding.domain.group.service.port.RedisGroupInviteService;
import com.tukorea.planding.domain.notify.dto.FcmDTO;
import com.tukorea.planding.domain.user.dto.AndroidLoginRequest;
import com.tukorea.planding.domain.user.dto.ProfileResponse;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.domain.user.service.port.UserCodeGenerator;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Builder
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserQueryService userQueryService;
    private final RedisGroupInviteService redisGroupInviteService;
    private final UserCodeGenerator userCodeGenerator;

    public UserDomain createAndroid(AndroidLoginRequest androidLoginRequest) {
        String userCode = userCodeGenerator.generateUniqueUserCode();
        UserDomain userDomain = UserDomain.androidCreate(androidLoginRequest, userCode);
        return userQueryService.save(userDomain);
    }

    //TODO 즐겨찾는 그룹, 그룹 요청
    // N+1문제는 발생하지 않지만 user조회쿼리 3개나감
    public ProfileResponse getProfile(Long userId) {
        UserDomain userDomain = userQueryService.getUserProfile(userId);
        return ProfileResponse.toProfileResponse(userDomain, redisGroupInviteService);
    }


    public void updateAlarmSetting(String userCode, boolean alarmSetting) {
        UserDomain user = userQueryService.getUserByUserCode(userCode);
        user = user.updateAlarm(alarmSetting);
        userQueryService.save(user);
    }

    @Transactional(readOnly = true)
    public String getFcmTokenByUserCode(String userCode) {
        UserDomain user = userQueryService.getUserByUserCode(userCode);
        return user.getFcmToken();
    }

    public void updateFcmToken(String userCode, FcmDTO fcmDTO) {
        UserDomain user = userQueryService.getUserByUserCode(userCode);
        log.info(userCode, fcmDTO.fcmToken());
        user = user.updateFcmToken(fcmDTO.fcmToken());
        userQueryService.save(user);
        log.info("FCM 토큰 업데이트 완료: 사용자 코드: {}, FCM 토큰: {}", userCode, fcmDTO.fcmToken());
    }

}
