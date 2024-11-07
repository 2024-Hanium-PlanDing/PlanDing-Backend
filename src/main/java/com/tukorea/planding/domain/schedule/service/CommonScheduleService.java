package com.tukorea.planding.domain.schedule.service;

import com.tukorea.planding.domain.schedule.dto.response.ScheduleResponse;
import com.tukorea.planding.domain.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonScheduleService {
    private final ScheduleQueryService scheduleQueryService;

    public List<ScheduleResponse> showTodaySchedule(UserResponse userResponse) {
        return scheduleQueryService.showTodaySchedule(userResponse.getId())
                .stream()
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
    }

    public List<ScheduleResponse> getWeekSchedule(LocalDate startDate, LocalDate endDate, UserResponse userResponse) {
        return scheduleQueryService.findWeeklyScheduleByUser(startDate, endDate, userResponse.getId())
                .stream()
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
    }
}
