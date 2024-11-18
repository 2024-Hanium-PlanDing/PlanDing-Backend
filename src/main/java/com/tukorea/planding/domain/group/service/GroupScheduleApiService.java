package com.tukorea.planding.domain.group.service;

import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.schedule.dto.response.GroupScheduleResponse;
import com.tukorea.planding.domain.schedule.dto.response.ScheduleResponse;
import com.tukorea.planding.domain.schedule.dto.response.UserScheduleAttendance;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.service.GroupScheduleAttendanceService;
import com.tukorea.planding.domain.schedule.service.ScheduleQueryService;
import com.tukorea.planding.domain.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupScheduleApiService {

    private final ScheduleQueryService scheduleQueryService;
    private final GroupQueryService groupQueryService;
    private final GroupScheduleAttendanceService groupScheduleAttendanceService;

    public List<ScheduleResponse> getWeekSchedule(LocalDate startDate, LocalDate endDate, UserResponse userResponse) {
        return scheduleQueryService.findWeeklyGroupScheduleByUser(startDate, endDate, userResponse.getId())
                .stream()
                .map(ScheduleResponse::from)
                .sorted(ScheduleResponse.getComparatorByStartTime())
                .collect(Collectors.toList());
    }

    public List<ScheduleResponse> getWeekScheduleByGroupCode(LocalDate startDate, LocalDate endDate, String groupRoomCode, UserResponse userResponse) {
        return scheduleQueryService.findWeeklyGroupScheduleByGroupCode(startDate, endDate, userResponse.getId(), groupRoomCode)
                .stream()
                .map(ScheduleResponse::from)
                .sorted(ScheduleResponse.getComparatorByStartTime())
                .collect(Collectors.toList());
    }

    public List<ScheduleResponse> getAllScheduleByGroup(LocalDate startDate, LocalDate endDate, UserResponse userResponse) {
        return scheduleQueryService.findAllGroupScheduleByGroupCode(startDate, endDate, userResponse.getId())
                .stream()
                .map(ScheduleResponse::from)
                .sorted(ScheduleResponse.getComparatorByStartTime())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GroupScheduleResponse getGroupScheduleById(String groupCode, Long scheduleId) {
        GroupRoom groupRoom = groupQueryService.getGroupByCode(groupCode);
        Schedule schedule = scheduleQueryService.findScheduleById(scheduleId);

        List<UserScheduleAttendance> attendances = groupScheduleAttendanceService.getUserScheduleAttendances(groupRoom, scheduleId);

        return GroupScheduleResponse.from(schedule, groupRoom.getName(), attendances);
    }

    @Transactional(readOnly = true)
    public List<GroupScheduleResponse> getSchedulesByGroupRoom(String groupCode) {
        List<Schedule> schedules = scheduleQueryService.findByGroupRoomCode(groupCode);
        GroupRoom groupRoom = groupQueryService.getGroupByCode(groupCode);

        return schedules.stream()
                .map(schedule -> GroupScheduleResponse.from(schedule, groupRoom.getName(), groupScheduleAttendanceService.getUserScheduleAttendances(groupRoom, schedule.getId())))
                .collect(Collectors.toList());
    }

}
