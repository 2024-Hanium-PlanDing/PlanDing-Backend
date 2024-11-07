package com.tukorea.planding.domain.notify.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.group.service.GroupRoomService;
import com.tukorea.planding.domain.user.dto.UserResponse;
import com.tukorea.planding.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Setting", description = "사용자 알림 설정 관련")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-setting")
public class UserNotificationSettingController {

    private final UserService userService;
    private final GroupRoomService groupRoomService;

    @Operation(description = "개인 스케줄 알림 설정을 업데이트한다")
    @PutMapping("/alarm")
    public CommonResponse<?> updateAlarmSetting(@AuthenticationPrincipal UserResponse userResponse, @RequestParam boolean alarmEnabled) {
        userService.updateAlarmSetting(userResponse.getUserCode(), alarmEnabled);
        return CommonUtils.successWithEmptyData();
    }

    @Operation(description = "그룹 스케줄 알림 설정을 업데이트한다")
    @PutMapping("/{groupCode}/alarm")
    public CommonResponse<?> updateGroupRoomAlarmSetting(@AuthenticationPrincipal UserResponse userResponse,
                                                         @PathVariable String groupCode,
                                                         @RequestParam boolean alarmEnabled) {
        groupRoomService.updateGroupRoomAlarmSetting(userResponse.getUserCode(), groupCode, alarmEnabled);
        return CommonUtils.successWithEmptyData();
    }
}
