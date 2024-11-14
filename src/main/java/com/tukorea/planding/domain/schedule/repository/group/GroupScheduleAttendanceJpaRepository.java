package com.tukorea.planding.domain.schedule.repository.group;

import com.tukorea.planding.domain.schedule.entity.GroupScheduleAttendance;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupScheduleAttendanceJpaRepository extends JpaRepository<GroupScheduleAttendance, Long> {
    Optional<GroupScheduleAttendance> findByUserIdAndScheduleIdAndStatusNot(Long userId, Long scheduleId, ScheduleStatus status);
    Optional<GroupScheduleAttendance> findByUserIdAndScheduleId(Long userId, Long scheduleId);
    Optional<GroupScheduleAttendance> findByUser_UserCodeAndSchedule_IdAndStatus(String userCode, Long scheduleId, ScheduleStatus status);
    void deleteBySchedule(Schedule schedule);

}
