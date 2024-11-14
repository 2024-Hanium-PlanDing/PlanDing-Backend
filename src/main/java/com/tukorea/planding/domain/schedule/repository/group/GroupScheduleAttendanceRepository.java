package com.tukorea.planding.domain.schedule.repository.group;

import com.tukorea.planding.domain.schedule.entity.GroupScheduleAttendance;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleStatus;
import com.tukorea.planding.domain.schedule.entity.domain.GroupScheduleAttendanceDomain;
import com.tukorea.planding.domain.schedule.entity.domain.ScheduleDomain;

import java.util.Optional;

public interface GroupScheduleAttendanceRepository {
    Optional<GroupScheduleAttendanceDomain> findByUserIdAndScheduleIdAndStatusNot(Long userId, Long scheduleId, ScheduleStatus status);

    Optional<GroupScheduleAttendanceDomain> findByUserIdAndScheduleId(Long userId, Long scheduleId);

    Optional<GroupScheduleAttendanceDomain> findByUser_UserCodeAndSchedule_IdAndStatus(String userCode, Long scheduleId, ScheduleStatus status);

    void deleteBySchedule(ScheduleDomain scheduleDomain);

    GroupScheduleAttendanceDomain save(GroupScheduleAttendanceDomain groupScheduleAttendanceDomain);

}
