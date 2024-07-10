package com.tukorea.planding.domain.schedule.repository;

import com.tukorea.planding.domain.schedule.entity.GroupScheduleAttendance;
import com.tukorea.planding.domain.schedule.entity.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupScheduleAttendanceRepository extends JpaRepository<GroupScheduleAttendance, Long> {
    Optional<GroupScheduleAttendance> findByUserIdAndScheduleIdAndStatusNot(Long userId, Long scheduleId, ScheduleStatus status);
    Optional<GroupScheduleAttendance> findByUserIdAndScheduleId(Long userId, Long scheduleId);
}
