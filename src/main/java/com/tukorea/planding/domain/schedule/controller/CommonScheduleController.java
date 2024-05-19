package com.tukorea.planding.domain.schedule.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.schedule.dto.request.ScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.response.PersonalScheduleResponse;
import com.tukorea.planding.domain.schedule.dto.response.ScheduleResponse;
import com.tukorea.planding.domain.schedule.service.CommonScheduleService;
import com.tukorea.planding.domain.user.dto.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "CommonSchedule", description = "공통 스케줄")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/common/schedule")
public class CommonScheduleController {

    private final CommonScheduleService commonScheduleService;

    @Operation(summary = "오늘 스케줄 조회")
    @GetMapping("/today")
    public CommonResponse<List<ScheduleResponse>> showTodaySchedule(@AuthenticationPrincipal UserInfo userInfo) {
        List<ScheduleResponse> responses = commonScheduleService.showTodaySchedule(userInfo);
        return CommonUtils.success(responses);
    }

    @Operation(summary = "공통: 스케쥴 생성을 할 때 내가 포함된 모든 그룹, 개인 시간때에 스케쥴을 확인")
    @PostMapping("/overlap")
    public CommonResponse<List<ScheduleResponse>> findOverlapSchedule(@AuthenticationPrincipal UserInfo userInfo, @RequestBody ScheduleRequest scheduleRequest) {
        List<ScheduleResponse> scheduleResponses = commonScheduleService.findOverlapSchedule(userInfo.getId(), scheduleRequest);
        return CommonUtils.success(scheduleResponses);
    }

    @Operation(summary = "주간으로 가져오기")
    @GetMapping("/week/{startDate}/{endDate}")
    public CommonResponse<List<ScheduleResponse>> getWeekSchedule(@PathVariable(name = "startDate") LocalDate startDate,
                                                                  @PathVariable(name = "endDate") LocalDate endDate
            , @AuthenticationPrincipal UserInfo userInfo) {
        List<ScheduleResponse> scheduleResponse = commonScheduleService.getWeekSchedule(startDate, endDate, userInfo);
        return CommonUtils.success(scheduleResponse);
    }
}
