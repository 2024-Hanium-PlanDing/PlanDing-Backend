package com.tukorea.planding.domain.schedule.service;

import com.tukorea.planding.domain.planner.dto.group.PlannerWeekResponse;
import com.tukorea.planding.domain.schedule.dto.request.ScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.response.ScheduleResponse;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.repository.GroupScheduleAttendanceRepository;
import com.tukorea.planding.domain.schedule.repository.GroupScheduleRepository;
import com.tukorea.planding.domain.schedule.repository.ScheduleRepository;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;
    private final GroupScheduleAttendanceRepository groupScheduleAttendanceRepository;

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

    public List<Schedule> findWeeklyGroupScheduleByUser(LocalDate startDate, LocalDate endDate, Long userId) {
        return scheduleRepository.findWeeklyGroupScheduleByUser(startDate, endDate, userId);
    }

    public List<Schedule> findWeeklyGroupScheduleByGroupCode(LocalDate startDate, LocalDate endDate, Long userId, String groupRoomCode) {
        return scheduleRepository.findWeeklyGroupScheduleByGroupCode(startDate, endDate, userId, groupRoomCode);
    }


    public Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND));
    }

    public List<Schedule> findByGroupRoomCode(String groupCode) {
        return scheduleRepository.findByGroupRoomCode(groupCode);
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

    public void deleteGroupScheduleById(Long scheduleId) {
        Schedule schedule = findScheduleById(scheduleId);
        groupScheduleAttendanceRepository.deleteBySchedule(schedule);
        scheduleRepository.delete(schedule);
    }

    public List<Schedule> findByUserAndScheduleDateBetween(Long userId, LocalDate startOfWeek, LocalDate endOfWeek) {
        return scheduleRepository.findByUserAndScheduleDateBetween(userId, startOfWeek, endOfWeek);
    }
}
