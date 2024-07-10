package com.tukorea.planding.domain.schedule.controller;

import com.tukorea.planding.domain.schedule.dto.request.GroupScheduleAttendanceRequest;
import com.tukorea.planding.domain.schedule.service.GroupScheduleAttendanceService;
import com.tukorea.planding.domain.user.dto.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> participationGroupSchedule(@AuthenticationPrincipal UserInfo userInfo, @RequestBody GroupScheduleAttendanceRequest status) {
        groupScheduleAttendanceService.participationGroupSchedule(userInfo, status);
        return new ResponseEntity<>("스케줄 참여여부 변경 완료되었습니다.", HttpStatus.OK);
    }
}
