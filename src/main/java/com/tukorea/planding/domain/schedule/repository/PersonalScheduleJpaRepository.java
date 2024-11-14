package com.tukorea.planding.domain.schedule.repository;

import com.tukorea.planding.domain.schedule.entity.PersonalSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalScheduleJpaRepository extends JpaRepository<PersonalSchedule, Long> {
}
