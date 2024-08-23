package com.tukorea.planding.domain.schedule.repository;

import com.tukorea.planding.domain.schedule.entity.PersonalSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalScheduleRepository extends JpaRepository<PersonalSchedule, Long>, PersonalScheduleRepositoryCustom {
}
