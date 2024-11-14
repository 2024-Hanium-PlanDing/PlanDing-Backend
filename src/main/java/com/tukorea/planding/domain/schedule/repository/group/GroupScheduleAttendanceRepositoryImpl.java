package com.tukorea.planding.domain.schedule.repository.group;

import com.tukorea.planding.domain.schedule.entity.GroupScheduleAttendance;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleStatus;
import com.tukorea.planding.domain.schedule.entity.domain.GroupScheduleAttendanceDomain;
import com.tukorea.planding.domain.schedule.entity.domain.ScheduleDomain;
import com.tukorea.planding.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GroupScheduleAttendanceRepositoryImpl implements GroupScheduleAttendanceRepository {

    private final GroupScheduleAttendanceJpaRepository groupScheduleAttendanceJpaRepository;

    @Override
    public Optional<GroupScheduleAttendanceDomain> findByUserIdAndScheduleIdAndStatusNot(Long userId, Long scheduleId, ScheduleStatus status) {
        return groupScheduleAttendanceJpaRepository.findByUserIdAndScheduleIdAndStatusNot(userId, scheduleId, status).map(GroupScheduleAttendance::toModel);
    }

    @Override
    public Optional<GroupScheduleAttendanceDomain> findByUserIdAndScheduleId(Long userId, Long scheduleId) {
        return groupScheduleAttendanceJpaRepository.findByUserIdAndScheduleId(userId, scheduleId).map(GroupScheduleAttendance::toModel);
    }

    @Override
    public Optional<GroupScheduleAttendanceDomain> findByUser_UserCodeAndSchedule_IdAndStatus(String userCode, Long scheduleId, ScheduleStatus status) {
        return groupScheduleAttendanceJpaRepository.findByUser_UserCodeAndSchedule_IdAndStatus(userCode, scheduleId, status).map(GroupScheduleAttendance::toModel);
    }

    @Override
    public void deleteBySchedule(ScheduleDomain scheduleDomain) {
        groupScheduleAttendanceJpaRepository.deleteBySchedule(Schedule.fromModel(scheduleDomain));
    }

    @Override
    public GroupScheduleAttendanceDomain save(GroupScheduleAttendanceDomain groupScheduleAttendanceDomain) {
        return groupScheduleAttendanceJpaRepository.save(GroupScheduleAttendance.fromModel(groupScheduleAttendanceDomain)).toModel();
    }
}
