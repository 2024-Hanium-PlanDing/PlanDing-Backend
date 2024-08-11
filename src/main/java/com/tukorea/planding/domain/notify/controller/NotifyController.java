package com.tukorea.planding.domain.notify.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.notify.dto.NotificationReadRequest;
import com.tukorea.planding.domain.notify.dto.NotificationScheduleResponse;
import com.tukorea.planding.domain.notify.service.NotificationHandler;
import com.tukorea.planding.domain.user.dto.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Tag(name = "Notify", description = "알림 메시지 관련")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotifyController {

    private final NotificationHandler notificationHandler;

    //TODO 어떤 방 알림을 구독할지 정해야함
    @Operation(description = "기본적인 알림을 받는다")
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal UserInfo userInfo) {
        return notificationHandler.subscribe(userInfo.getUserCode());
    }

    @Operation(description = "스케줄 타입 알람 목록 가져온다")
    @GetMapping("/schedules")
    public CommonResponse<List<NotificationScheduleResponse>> getNotifications(@AuthenticationPrincipal UserInfo userInfo){
        List<NotificationScheduleResponse> response = notificationHandler.getNotifications(userInfo);
        return CommonUtils.success(response);
    }

    @Operation(description = "스케줄 타입 알람을 삭제한다")
    @DeleteMapping("/{scheduleId}")
    public CommonResponse<?> deleteNotification(@AuthenticationPrincipal UserInfo userInfo,@PathVariable Long scheduleId){
        notificationHandler.deleteNotification(userInfo,scheduleId);
        return CommonUtils.successWithEmptyData();
    }

    @Operation(description = "스케줄 타입의 알람을 읽는다")
    @PostMapping("/read")
    public CommonResponse<?> markNotificationAsRead(@RequestBody NotificationReadRequest notificationReadRequest) {
        notificationHandler.markNotificationAsRead(notificationReadRequest);
        return CommonUtils.successWithEmptyData();
    }

}
