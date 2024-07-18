package com.tukorea.planding.domain.schedule.service;

import com.tukorea.planding.domain.schedule.dto.request.ScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.response.ScheduleResponse;
import com.tukorea.planding.domain.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommonScheduleService {
    private final ScheduleQueryService scheduleQueryService;

    @Transactional(readOnly = true)
    public List<ScheduleResponse> showTodaySchedule(UserInfo userInfo) {
        return scheduleQueryService.showTodaySchedule(userInfo.getId())
                .stream()
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getWeekSchedule(LocalDate startDate, LocalDate endDate, UserInfo userInfo) {
        return scheduleQueryService.findWeeklyScheduleByUser(startDate, endDate, userInfo.getId())
                .stream()
                .map(ScheduleResponse::from)
                .sorted(ScheduleResponse.getComparatorByStartTime())
                .collect(Collectors.toList());
    }
}
