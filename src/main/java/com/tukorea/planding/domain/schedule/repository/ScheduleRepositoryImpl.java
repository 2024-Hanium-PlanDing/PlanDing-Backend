package com.tukorea.planding.domain.schedule.repository;

import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.domain.ScheduleDomain;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final ScheduleJpaRepository scheduleJpaRepository;

    @Override
    public List<ScheduleDomain> findAllByScheduleDate(LocalDate today) {
        return scheduleJpaRepository.findAllByScheduleDate(today).stream()
                .map(Schedule::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDomain> findWeeklyScheduleByUser(LocalDate startDate, LocalDate endDate, Long userId) {
        return scheduleJpaRepository.findWeeklyScheduleByUser(startDate, endDate, userId).stream()
                .map(Schedule::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDomain> findWeeklyPersonalScheduleByUser(LocalDate startDate, LocalDate endDate, Long userId) {
        return scheduleJpaRepository.findWeeklyPersonalScheduleByUser(startDate, endDate, userId).stream()
                .map(Schedule::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDomain> findWeeklyGroupScheduleByUser(LocalDate startDate, LocalDate endDate, Long userId) {
        return scheduleJpaRepository.findWeeklyGroupScheduleByUser(startDate, endDate, userId).stream()
                .map(Schedule::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDomain> findWeeklyGroupScheduleByGroupCode(LocalDate startDate, LocalDate endDate, Long userId, String groupRoomCode) {
        return scheduleJpaRepository.findWeeklyGroupScheduleByGroupCode(startDate, endDate, userId, groupRoomCode).stream()
                .map(Schedule::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDomain> findOverlapSchedules(Long userId, LocalDate date, Integer startDate, Integer endDate) {
        return scheduleJpaRepository.findOverlapSchedules(userId, date, startDate, endDate).stream()
                .map(Schedule::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDomain> showTodaySchedule(Long userId) {
        return scheduleJpaRepository.showTodaySchedule(userId).stream()
                .map(Schedule::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDomain> findAllGroupScheduleByGroupCode(LocalDate startDate, LocalDate endDate, Long userId) {
        return scheduleJpaRepository.findAllGroupScheduleByGroupCode(startDate, endDate, userId).stream()
                .map(Schedule::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDomain> findByGroupRoomCode(String groupCode) {
        return scheduleJpaRepository.findByGroupRoomCode(groupCode).stream()
                .map(Schedule::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDomain> findByUserAndScheduleDateBetween(Long userId, LocalDate startOfWeek, LocalDate endOfWeek) {
        return scheduleJpaRepository.findByUserAndScheduleDateBetween(userId, startOfWeek, endOfWeek).stream()
                .map(Schedule::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleDomain findById(Long id) {
        return scheduleJpaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND))
                .toModel();
    }

    @Override
    public ScheduleDomain save(ScheduleDomain scheduleDomain) {
        return scheduleJpaRepository.save(Schedule.fromModel(scheduleDomain)).toModel();
    }

    @Override
    public void delete(ScheduleDomain scheduleDomain) {
        scheduleJpaRepository.delete(Schedule.fromModel(scheduleDomain));
    }
}
