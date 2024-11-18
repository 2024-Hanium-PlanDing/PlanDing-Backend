package com.tukorea.planding.domain.schedule.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.schedule.dto.request.websocket.SendCreateScheduleDTO;
import com.tukorea.planding.domain.schedule.dto.request.websocket.SendDeleteScheduleDTO;
import com.tukorea.planding.domain.schedule.dto.request.websocket.SendUpdateScheduleDTO;
import com.tukorea.planding.domain.schedule.dto.response.GroupScheduleResponse;
import com.tukorea.planding.domain.schedule.service.GroupScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "GroupSchedule", description = "그룹 스케줄")
@RestController
@RequiredArgsConstructor
public class GroupScheduleWebsocketController {

    private final GroupScheduleService groupScheduleService;

    @MessageMapping("/schedule/create/{groupCode}") // schedule 경로로 메시지를 보내면
    @SendTo("/sub/schedule/{groupCode}")    // /sub/schedule/{group_code} 을 구독한 유저에게 메시지를 뿌림
    public CommonResponse<?> createGroupSchedule(StompHeaderAccessor accessor, @DestinationVariable String groupCode, @Valid SendCreateScheduleDTO request) {
        String userCode = accessor.getSessionAttributes().get("userCode").toString();
        return CommonUtils.success(groupScheduleService.createGroupSchedule(userCode, groupCode, request));
    }

    @MessageMapping("/schedule/update/{groupCode}") // schedule 경로로 메시지를 보내면
    @SendTo("/sub/schedule/{groupCode}")    // /sub/schedule/{group_code} 을 구독한 유저에게 메시지를 뿌림
    public CommonResponse<?> updateGroupSchedule(StompHeaderAccessor accessor, @DestinationVariable String groupCode, @Valid SendUpdateScheduleDTO request) {
        String userCode = accessor.getSessionAttributes().get("userCode").toString();
        return CommonUtils.success(groupScheduleService.updateScheduleByGroupRoom(userCode, groupCode, request));
    }

    @MessageMapping("/schedule/delete/{groupCode}") // schedule 경로로 메시지를 보내면
    @SendTo("/sub/schedule/{groupCode}")    // /sub/schedule/{group_code} 을 구독한 유저에게 메시지를 뿌림
    public CommonResponse<?> deleteGroupSchedule(StompHeaderAccessor accessor, @DestinationVariable String groupCode, @Valid SendDeleteScheduleDTO request) {
        String userCode = accessor.getSessionAttributes().get("userCode").toString();
        return CommonUtils.success(groupScheduleService.deleteScheduleByGroupRoom(userCode, groupCode, request));

    }
}
