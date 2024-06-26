package com.tukorea.planding.domain.schedule.service;

import com.tukorea.planding.domain.schedule.dto.request.ScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.response.PersonalScheduleResponse;
import com.tukorea.planding.domain.schedule.dto.response.ScheduleResponse;
import com.tukorea.planding.domain.schedule.entity.PersonalSchedule;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.repository.ScheduleRepository;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;

    public List<ScheduleResponse> findOverlapSchedule(Long userId, ScheduleRequest scheduleRequest) {
        List<Schedule> overlapSchedules = scheduleRepository.findOverlapSchedules(userId, scheduleRequest.scheduleDate(), scheduleRequest.startTime(), scheduleRequest.endTime());
        return overlapSchedules.stream()
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
    }

    public List<Schedule> findWeeklyScheduleByUser(LocalDate startDate, LocalDate endDate, Long userId) {
        return scheduleRepository.findWeeklyScheduleByUser(startDate, endDate, userId);
    }

    public List<Schedule> findWeeklyPersonalScheduleByUser(LocalDate startDate, LocalDate endDate, Long userId) {
        return scheduleRepository.findWeeklyPersonalScheduleByUser(startDate, endDate, userId);
    }

    public Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND));
    }

    public List<Schedule> findByGroupRoomId(Long groupRoomId) {
        return scheduleRepository.findByGroupRoomId(groupRoomId);
    }

    public List<Schedule> showTodaySchedule(Long userId) {
        return scheduleRepository.showTodaySchedule(userId);
    }

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public void delete(Schedule schedule) {
        scheduleRepository.delete(schedule);
    }

    public void deleteById(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    public List<Schedule> findByUserAndScheduleDateBetween(Long userId, LocalDate startOfWeek, LocalDate endOfWeek) {
        return scheduleRepository.findByUserAndScheduleDateBetween(userId, startOfWeek, endOfWeek);
    }
}
