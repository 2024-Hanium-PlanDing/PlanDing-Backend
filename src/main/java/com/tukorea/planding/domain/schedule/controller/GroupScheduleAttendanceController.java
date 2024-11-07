package com.tukorea.planding.domain.schedule.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.schedule.dto.request.GroupScheduleAttendanceRequest;
import com.tukorea.planding.domain.schedule.service.GroupScheduleAttendanceService;
import com.tukorea.planding.domain.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "GroupAttendance", description = "그룹 스케줄 참여설정")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attendance")
public class GroupScheduleAttendanceController {

    private final GroupScheduleAttendanceService groupScheduleAttendanceService;

    @PostMapping()
    @Operation(summary = "스케줄 참여 여부 선택")
    public CommonResponse<?> participationGroupSchedule(@AuthenticationPrincipal UserResponse userResponse, @RequestBody GroupScheduleAttendanceRequest status) {
        return CommonUtils.success(groupScheduleAttendanceService.participationGroupSchedule(userResponse, status));
    }
}
