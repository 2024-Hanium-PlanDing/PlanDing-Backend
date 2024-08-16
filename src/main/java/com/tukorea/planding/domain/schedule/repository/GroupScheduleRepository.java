package com.tukorea.planding.domain.schedule.repository;

import com.tukorea.planding.domain.schedule.entity.GroupSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupScheduleRepository extends JpaRepository<GroupSchedule, Long> {
    List<GroupSchedule> findAllByGroupRoomId(Long groupId);
}
