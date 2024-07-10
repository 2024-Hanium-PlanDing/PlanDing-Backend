package com.tukorea.planding.domain.schedule.repository;

import com.tukorea.planding.domain.schedule.entity.PersonalSchedule;

import java.time.LocalDate;
import java.util.List;

public interface PersonalScheduleRepositoryCustom {
    List<PersonalSchedule> findSchedulesForNextDay(LocalDate date);
}
