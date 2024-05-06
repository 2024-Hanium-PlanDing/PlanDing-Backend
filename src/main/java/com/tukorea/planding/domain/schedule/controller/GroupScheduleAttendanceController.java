package com.tukorea.planding.domain.schedule.controller;

import com.tukorea.planding.domain.schedule.dto.GroupScheduleAttendanceRequest;
import com.tukorea.planding.domain.schedule.service.GroupScheduleAttendanceService;
import com.tukorea.planding.domain.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attendance")
public class GroupScheduleAttendanceController {

    private final GroupScheduleAttendanceService groupScheduleAttendanceService;

    @GetMapping()
    public ResponseEntity<?> participationGroupSchedule(@AuthenticationPrincipal UserInfo userInfo, GroupScheduleAttendanceRequest status) {
        groupScheduleAttendanceService.participationGroupSchedule(userInfo.getUserCode(), status);
        return new ResponseEntity<>("스케줄 참여여부 변경 완료되었습니다.", HttpStatus.OK);
    }
}
