package com.tukorea.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fcm")
public class FireBaseController {

    private final UserService userService;

    @PostMapping("/token")
    public CommonResponse<?> saveNotification(@AuthenticationPrincipal UserInfo userInfo, @RequestBody String token) {
        userService.updateFcmToken(userInfo.getUserCode(), token);
        return CommonUtils.successWithEmptyData();
    }
}
