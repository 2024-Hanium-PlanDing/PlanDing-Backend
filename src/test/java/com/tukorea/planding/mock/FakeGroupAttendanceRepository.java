package com.tukorea.planding.mock;

import com.tukorea.planding.domain.schedule.entity.ScheduleStatus;
import com.tukorea.planding.domain.schedule.entity.domain.GroupScheduleAttendanceDomain;
import com.tukorea.planding.domain.schedule.entity.domain.ScheduleDomain;
import com.tukorea.planding.domain.schedule.repository.group.GroupScheduleAttendanceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeGroupAttendanceRepository implements GroupScheduleAttendanceRepository {

    private final List<GroupScheduleAttendanceDomain> data = new ArrayList<>();

    @Override
    public Optional<GroupScheduleAttendanceDomain> findByUserIdAndScheduleIdAndStatusNot(Long userId, Long scheduleId, ScheduleStatus status) {
        return data.stream()
                .filter(attendance -> attendance.getUser().getId().equals(userId)
                        && attendance.getSchedule().getId().equals(scheduleId)
                        && !attendance.getStatus().equals(status))
                .findFirst();
    }

    @Override
    public Optional<GroupScheduleAttendanceDomain> findByUserIdAndScheduleId(Long userId, Long scheduleId) {
        return data.stream()
                .filter(attendance -> attendance.getUser().getId().equals(userId)
                        && attendance.getSchedule().getId().equals(scheduleId))
                .findFirst();
    }

    @Override
    public Optional<GroupScheduleAttendanceDomain> findByUser_UserCodeAndSchedule_IdAndStatus(String userCode, Long scheduleId, ScheduleStatus status) {
        return data.stream()
                .filter(attendance -> attendance.getUser().getUserCode().equals(userCode)
                        && attendance.getSchedule().getId().equals(scheduleId)
                        && attendance.getStatus().equals(status))
                .findFirst();
    }

    @Override
    public void deleteBySchedule(ScheduleDomain scheduleDomain) {
        data.removeIf(attendance -> attendance.getSchedule().equals(scheduleDomain));
    }

    @Override
    public GroupScheduleAttendanceDomain save(GroupScheduleAttendanceDomain groupScheduleAttendanceDomain) {
        data.removeIf(existing -> existing.getUser().getId().equals(groupScheduleAttendanceDomain.getUser().getId())
                && existing.getSchedule().getId().equals(groupScheduleAttendanceDomain.getSchedule().getId()));
        data.add(groupScheduleAttendanceDomain);
        return groupScheduleAttendanceDomain;
    }
}