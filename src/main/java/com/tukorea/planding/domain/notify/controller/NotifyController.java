package com.tukorea.planding.domain.notify.controller;

import com.tukorea.planding.domain.notify.service.NotificationHandler;
import com.tukorea.planding.domain.user.dto.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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

}
