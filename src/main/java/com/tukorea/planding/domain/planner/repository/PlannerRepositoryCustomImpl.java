package com.tukorea.planding.domain.planner.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tukorea.planding.domain.group.entity.QGroupRoom;
import com.tukorea.planding.domain.planner.entity.Planner;
import com.tukorea.planding.domain.schedule.entity.QGroupSchedule;
import com.tukorea.planding.domain.schedule.entity.QSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.tukorea.planding.domain.group.entity.QGroupRoom.groupRoom;
import static com.tukorea.planding.domain.planner.entity.QPlanner.planner;
import static com.tukorea.planding.domain.schedule.entity.QGroupSchedule.groupSchedule;
import static com.tukorea.planding.domain.schedule.entity.QSchedule.schedule;

@Repository
@RequiredArgsConstructor
public class PlannerRepositoryCustomImpl implements PlannerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Planner> findPlannersByGroupCodeAndDateRange(String groupCode, LocalDate startDate, LocalDate endDate) {
        QSchedule schedule = QSchedule.schedule;
        QGroupSchedule groupSchedule = QGroupSchedule.groupSchedule;
        QGroupRoom groupRoom = QGroupRoom.groupRoom;

        return queryFactory.selectFrom(planner)
                .join(planner.schedule, schedule)
                .join(schedule.groupSchedule, groupSchedule)
                .join(groupSchedule.groupRoom, groupRoom)
                .where(
                        groupRoom.groupCode.eq(groupCode)
                                .and(schedule.scheduleDate.between(startDate, endDate))
                )
                .groupBy(schedule.id, schedule.scheduleDate, schedule.title) // GROUP BY에 모든 비집계 열 추가
                .fetch();
    }
}
