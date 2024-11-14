package com.tukorea.planding.domain.schedule.repository.group;

import com.tukorea.planding.domain.schedule.entity.GroupSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupScheduleJpaRepository extends JpaRepository<GroupSchedule, Long> {
    List<GroupSchedule> findAllByGroupRoomId(Long groupId);
}
