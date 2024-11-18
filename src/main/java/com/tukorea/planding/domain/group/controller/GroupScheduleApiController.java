package com.tukorea.planding.domain.group.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.group.service.GroupScheduleApiService;
import com.tukorea.planding.domain.schedule.dto.response.GroupScheduleResponse;
import com.tukorea.planding.domain.schedule.dto.response.ScheduleResponse;
import com.tukorea.planding.domain.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/group-rooms")
@RequiredArgsConstructor
public class GroupScheduleApiController {

    private final GroupScheduleApiService groupScheduleApiService;

    @Operation(summary = "그룹: 주간으로 가져오기")
    @GetMapping("/week/{startDate}/{endDate}")
    public CommonResponse<List<ScheduleResponse>> getWeekSchedule(@PathVariable(name = "startDate") LocalDate startDate,
                                                                  @PathVariable(name = "endDate") LocalDate endDate
            , @AuthenticationPrincipal UserResponse userResponse) {
        List<ScheduleResponse> scheduleResponse = groupScheduleApiService.getWeekSchedule(startDate, endDate, userResponse);
        return CommonUtils.success(scheduleResponse);
    }

    @Operation(summary = "그룹: 그룹에 들어갔을때 주간으로 가져오기")
    @GetMapping("/week/{groupCode}/{startDate}/{endDate}")
    public CommonResponse<List<ScheduleResponse>> getWeekScheduleByGroup(@PathVariable String groupCode, @PathVariable LocalDate startDate,
                                                                         @PathVariable LocalDate endDate
            , @AuthenticationPrincipal UserResponse userResponse) {
        List<ScheduleResponse> scheduleResponse = groupScheduleApiService.getWeekScheduleByGroupCode(startDate, endDate, groupCode, userResponse);
        return CommonUtils.success(scheduleResponse);
    }

    @Operation(summary = "그룹: 그룹 전체 데이터 조회")
    @GetMapping("/all/{startDate}/{endDate}")
    public CommonResponse<List<ScheduleResponse>> getAllScheduleByGroup(@AuthenticationPrincipal UserResponse userResponse, @PathVariable LocalDate startDate,
                                                                        @PathVariable LocalDate endDate) {
        List<ScheduleResponse> scheduleResponse = groupScheduleApiService.getAllScheduleByGroup(startDate, endDate, userResponse);
        return CommonUtils.success(scheduleResponse);
    }

    @Operation(summary = "그룹 스케줄: 작성 목록 조회")
    @GetMapping("/{groupCode}")
    public CommonResponse<List<GroupScheduleResponse>> getSchedulesByGroupRoom(@PathVariable String groupCode) {
        List<GroupScheduleResponse> scheduleResponses = groupScheduleApiService.getSchedulesByGroupRoom(groupCode);
        return CommonUtils.success(scheduleResponses);
    }

    @Operation(summary = "그룹 스케줄: 조회")
    @GetMapping("/{groupCode}/{scheduleId}")
    public CommonResponse<GroupScheduleResponse> getGroupSchedule(@PathVariable String groupCode, @PathVariable Long scheduleId) {
        GroupScheduleResponse scheduleResponses = groupScheduleApiService.getGroupScheduleById(groupCode, scheduleId);
        return CommonUtils.success(scheduleResponses);
    }
}
