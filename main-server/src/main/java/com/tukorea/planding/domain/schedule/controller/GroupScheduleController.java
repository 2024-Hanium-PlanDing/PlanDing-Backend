package com.tukorea.planding.domain.schedule.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.schedule.dto.request.GroupScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.response.GroupScheduleResponse;
import com.tukorea.planding.domain.schedule.dto.response.ScheduleResponse;
import com.tukorea.planding.domain.schedule.service.GroupScheduleService;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "GroupSchedule", description = "그룹 스케줄")
@RestController
@RequiredArgsConstructor
public class GroupScheduleController {

    private final GroupScheduleService groupScheduleService;

    @MessageMapping("/schedule/{groupCode}") // schedule 경로로 메시지를 보내면
    @SendTo("/sub/schedule/{groupCode}")    // /sub/schedule/{group_code} 을 구독한 유저에게 메시지를 뿌림
    public CommonResponse<?> handleGroupSchedule(@DestinationVariable String groupCode, @Valid GroupScheduleRequest request) {
        return switch (request.action()) {
            case CREATE -> CommonUtils.success(groupScheduleService.createGroupSchedule(groupCode, request));
            case UPDATE -> CommonUtils.success(groupScheduleService.updateScheduleByGroupRoom(groupCode, request));
            case DELETE -> {
                groupScheduleService.deleteScheduleByGroupRoom(groupCode, request);
                yield null;
            }
            default -> throw new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND);
        };
    }


    @Operation(summary = "그룹 스케줄: 작성 목록 조회")
    @GetMapping("/api/v1/group-rooms/{groupCode}")
    public CommonResponse<List<GroupScheduleResponse>> getSchedulesByGroupRoom(@PathVariable String groupCode, @AuthenticationPrincipal UserInfo userInfo) {
        List<GroupScheduleResponse> scheduleResponses = groupScheduleService.getSchedulesByGroupRoom(groupCode, userInfo);
        return CommonUtils.success(scheduleResponses);
    }

    @Operation(summary = "그룹 스케줄: 조회")
    @GetMapping("/api/v1/group-rooms/{groupCode}/{scheduleId}")
    public CommonResponse<GroupScheduleResponse> getGroupSchedule(@PathVariable String groupCode, @PathVariable Long scheduleId, @AuthenticationPrincipal UserInfo userInfo) {
        GroupScheduleResponse scheduleResponses = groupScheduleService.getGroupScheduleById(userInfo, groupCode, scheduleId);
        return CommonUtils.success(scheduleResponses);
    }

    @Operation(summary = "그룹: 주간으로 가져오기")
    @GetMapping("/api/v1/group-rooms/week/{startDate}/{endDate}")
    public CommonResponse<List<ScheduleResponse>> getWeekSchedule(@PathVariable(name = "startDate") LocalDate startDate,
                                                                  @PathVariable(name = "endDate") LocalDate endDate
            , @AuthenticationPrincipal UserInfo userInfo) {
        List<ScheduleResponse> scheduleResponse = groupScheduleService.getWeekSchedule(startDate, endDate, userInfo);
        return CommonUtils.success(scheduleResponse);
    }

    @Operation(summary = "그룹: 그룹에 들어갔을때 주간으로 가져오기")
    @GetMapping("/api/v1/group-rooms/week/{groupCode}/{startDate}/{endDate}")
    public CommonResponse<List<ScheduleResponse>> getWeekScheduleByGroup(@PathVariable String groupCode, @PathVariable LocalDate startDate,
                                                                         @PathVariable LocalDate endDate
            , @AuthenticationPrincipal UserInfo userInfo) {
        List<ScheduleResponse> scheduleResponse = groupScheduleService.getWeekScheduleByGroupCode(startDate, endDate, groupCode, userInfo);
        return CommonUtils.success(scheduleResponse);
    }
}
