package com.tukorea.planding.domain.schedule.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleStatus;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.tukorea.planding.domain.group.entity.QGroupRoom.groupRoom;
import static com.tukorea.planding.domain.group.entity.QUserGroup.userGroup;
import static com.tukorea.planding.domain.schedule.entity.QGroupSchedule.groupSchedule;
import static com.tukorea.planding.domain.schedule.entity.QGroupScheduleAttendance.groupScheduleAttendance;
import static com.tukorea.planding.domain.schedule.entity.QPersonalSchedule.personalSchedule;
import static com.tukorea.planding.domain.schedule.entity.QSchedule.schedule;

@RequiredArgsConstructor
public class ScheduleRepositoryCustomImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Schedule> findWeeklyScheduleByUser(LocalDate startDate, LocalDate endDate, Long userId) {
        return queryFactory.selectFrom(schedule)
                .leftJoin(schedule.personalSchedule, personalSchedule)
                .leftJoin(schedule.groupSchedule, groupSchedule)
                .where(schedule.scheduleDate.between(startDate, endDate)
                        .and(personalSchedule.user.id.eq(userId)
                                .or(groupSchedule.groupRoom.userGroups.any().user.id.eq(userId))))
                .orderBy(schedule.startTime.asc())
                .fetch();
    }

    @Override
    public List<Schedule> findWeeklyPersonalScheduleByUser(LocalDate startDate, LocalDate endDate, Long userId) {
        return queryFactory.selectFrom(schedule)
                .innerJoin(schedule.personalSchedule, personalSchedule)
                .where(schedule.scheduleDate.between(startDate, endDate)
                        .and(personalSchedule.user.id.eq(userId)))
                .fetch();
    }

    @Override
    public List<Schedule> findWeeklyGroupScheduleByUser(LocalDate startDate, LocalDate endDate, Long userId) {
        return queryFactory.selectFrom(schedule)
                .leftJoin(schedule.groupSchedule, groupScheduleAttendance.schedule.groupSchedule)
                .where(schedule.scheduleDate.between(startDate, endDate)
                        .and(groupScheduleAttendance.user.id.eq(userId))
                        .and(groupScheduleAttendance.status.eq(ScheduleStatus.POSSIBLE)))
                .fetch();
    }

    @Override
    public List<Schedule> findWeeklyGroupScheduleByGroupCode(LocalDate startDate, LocalDate endDate, Long userId, String groupRoomCode) {

        return queryFactory.selectFrom(schedule)
                .leftJoin(schedule.groupSchedule, groupSchedule)
                .leftJoin(groupSchedule.groupRoom, groupRoom)
                .leftJoin(groupScheduleAttendance)
                .on(groupScheduleAttendance.schedule.eq(schedule)
                        .and(groupScheduleAttendance.user.id.eq(userId))
                        .and(groupScheduleAttendance.status.eq(ScheduleStatus.POSSIBLE)))
                .where(schedule.scheduleDate.between(startDate, endDate)
                        .and(groupRoom.groupCode.eq(groupRoomCode)))
                .fetch();
    }


    @Override
    public List<Schedule> findOverlapSchedules(Long userId, LocalDate date, Integer startTime, Integer endTime) {
        return queryFactory.selectFrom(schedule)
                .leftJoin(schedule.personalSchedule, personalSchedule)
                .where(
                        schedule.scheduleDate.eq(date)
                                .and(schedule.startTime.lt(endTime).and(schedule.endTime.gt(startTime))
                                        .or(schedule.startTime.between(startTime, endTime))
                                        .or(schedule.endTime.between(startTime, endTime)))
                                .and(personalSchedule.user.id.eq(userId)))
                .fetch();
    }

    @Override
    public List<Schedule> showTodaySchedule(Long userId) {
        LocalDate today = LocalDate.now();

        return queryFactory.select(schedule)
                .from(schedule)
                .leftJoin(schedule.personalSchedule, personalSchedule)
                .leftJoin(schedule.groupSchedule, groupSchedule)
                .leftJoin(groupSchedule.groupRoom, groupRoom)
                .leftJoin(groupRoom.userGroups, userGroup)
                .where(
                        schedule.scheduleDate.eq(today)
                                .and(
                                        personalSchedule.user.id.eq(userId)
                                                .or(userGroup.user.id.eq(userId))
                                )
                )
                .orderBy(schedule.startTime.asc())
                .fetch();
    }

    @Override
    public List<Schedule> findAllGroupScheduleByGroupCode(LocalDate startDate, LocalDate endDate, Long userId) {
        return queryFactory.selectFrom(schedule)
                .leftJoin(schedule.groupSchedule, groupSchedule)
                .where(schedule.scheduleDate.between(startDate, endDate).and(
                        groupSchedule.groupRoom.userGroups.any().user.id.eq(userId)
                ))
                .fetch();
    }

    @Override
    public List<Schedule> showTodaySchedule(String identity) {
        LocalDate today = LocalDate.now();

        return queryFactory.selectFrom(schedule)
                .leftJoin(schedule.personalSchedule, personalSchedule)
                .leftJoin(schedule.groupSchedule, groupSchedule)
                .where(
                        schedule.scheduleDate.eq(today)
                                .and(
                                        personalSchedule.user.userCode.eq(identity)
                                                .or(groupSchedule.groupRoom.userGroups.any().user.userCode.eq(identity))
                                )
                )
                .fetch();
    }

    @Override
    public List<Schedule> findByGroupRoomCode(String groupCode) {
        return queryFactory.selectFrom(schedule)
                .join(schedule.groupSchedule, groupSchedule)
                .where(groupSchedule.groupRoom.groupCode.eq(groupCode))
                .fetch();
    }

    @Override
    public List<Schedule> findByUserAndScheduleDateBetween(Long userId, LocalDate startOfWeek, LocalDate endOfWeek) {
        return queryFactory.selectFrom(schedule)
                .join(schedule.personalSchedule)
                .where(personalSchedule.user.id.eq(userId).and(schedule.scheduleDate.between(startOfWeek, endOfWeek)))
                .fetch();
    }
}
