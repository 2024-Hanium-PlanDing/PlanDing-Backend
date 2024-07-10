package com.tukorea.planding.domain.schedule.repository;

import com.tukorea.planding.domain.schedule.entity.GroupSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupScheduleRepository extends JpaRepository<GroupSchedule, Long> {
}
