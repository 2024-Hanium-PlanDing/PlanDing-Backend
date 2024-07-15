package com.tukorea.planding.domain.schedule.repository;

import com.tukorea.planding.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {
    List<Schedule> findAllByScheduleDate(LocalDate today);
}
