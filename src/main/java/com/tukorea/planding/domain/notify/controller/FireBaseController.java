package com.tukorea.planding.domain.notify.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.notify.dto.FcmDTO;
import com.tukorea.planding.domain.user.dto.UserResponse;
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
    public CommonResponse<?> saveNotification(@AuthenticationPrincipal UserResponse userResponse, @RequestBody FcmDTO fcmDTO) {
        userService.updateFcmToken(userResponse.getUserCode(), fcmDTO);
        return CommonUtils.successWithEmptyData();
    }
}
