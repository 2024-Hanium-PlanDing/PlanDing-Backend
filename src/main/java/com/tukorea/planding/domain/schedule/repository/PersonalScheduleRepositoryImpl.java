package com.tukorea.planding.domain.schedule.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tukorea.planding.domain.schedule.entity.PersonalSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.tukorea.planding.domain.schedule.entity.QPersonalSchedule.personalSchedule;

@Repository
@RequiredArgsConstructor
public class PersonalScheduleRepositoryImpl implements PersonalScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PersonalSchedule> findSchedulesForNextDay(LocalDate date) {
        LocalDate nextDay = date.plusDays(1);

        return queryFactory.selectFrom(personalSchedule)
                .where(personalSchedule.schedules.any().scheduleDate.eq(nextDay))
                .fetch();
    }
}
