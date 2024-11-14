package com.tukorea.planding.mock;

import com.tukorea.planding.domain.schedule.entity.ScheduleType;
import com.tukorea.planding.domain.schedule.entity.domain.ScheduleDomain;
import com.tukorea.planding.domain.schedule.repository.ScheduleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeScheduleRepository implements ScheduleRepository {

    private long count = 1L; // 초기 ID 값
    private final List<ScheduleDomain> data = new ArrayList<>();

    @Override
    public List<ScheduleDomain> findAllByScheduleDate(LocalDate today) {
        return data.stream()
                .filter(schedule -> today.equals(schedule.getScheduleDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDomain> findWeeklyScheduleByUser(LocalDate startDate, LocalDate endDate, Long userId) {
        return data.stream()
                .filter(schedule -> !schedule.getScheduleDate().isBefore(startDate)
                        && !schedule.getScheduleDate().isAfter(endDate)
                        && userId.equals(schedule.getPersonalSchedule().getUser().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDomain> findWeeklyPersonalScheduleByUser(LocalDate startDate, LocalDate endDate, Long userId) {
        return data.stream()
                .filter(schedule -> !schedule.getScheduleDate().isBefore(startDate)
                        && !schedule.getScheduleDate().isAfter(endDate)
                        && userId.equals(schedule.getPersonalSchedule().getUser().getId())
                        && schedule.getType() == ScheduleType.PERSONAL)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDomain> findWeeklyGroupScheduleByUser(LocalDate startDate, LocalDate endDate, Long userId) {
        return data.stream()
                .filter(schedule -> !schedule.getScheduleDate().isBefore(startDate)
                        && !schedule.getScheduleDate().isAfter(endDate)
                        && userId.equals(schedule.getPersonalSchedule().getUser().getId())
                        && schedule.getType() == ScheduleType.GROUP)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDomain> findWeeklyGroupScheduleByGroupCode(LocalDate startDate, LocalDate endDate, Long userId, String groupRoomCode) {
        return data.stream()
                .filter(schedule -> !schedule.getScheduleDate().isBefore(startDate)
                        && !schedule.getScheduleDate().isAfter(endDate)
                        && userId.equals(schedule.getPersonalSchedule().getUser().getId())
                        && groupRoomCode.equals(schedule.getGroupSchedule().getGroupRoom().getGroupCode()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDomain> findOverlapSchedules(Long userId, LocalDate date, Integer startTime, Integer endTime) {
        return data.stream()
                .filter(schedule -> userId.equals(schedule.getPersonalSchedule().getUser().getId())
                        && date.equals(schedule.getScheduleDate())
                        && ((schedule.getStartTime() < endTime && schedule.getEndTime() > startTime)))
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDomain> showTodaySchedule(Long userId) {
        LocalDate today = LocalDate.now();
        return data.stream()
                .filter(schedule -> today.equals(schedule.getScheduleDate())
                        && userId.equals(schedule.getPersonalSchedule().getUser().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDomain> findAllGroupScheduleByGroupCode(LocalDate startDate, LocalDate endDate, Long userId) {
        return data.stream()
                .filter(schedule -> !schedule.getScheduleDate().isBefore(startDate)
                        && !schedule.getScheduleDate().isAfter(endDate)
                        && userId.equals(schedule.getPersonalSchedule().getUser().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDomain> findByGroupRoomCode(String groupCode) {
        return data.stream()
                .filter(schedule -> groupCode.equals(schedule.getGroupSchedule().getGroupRoom().getGroupCode()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDomain> findByUserAndScheduleDateBetween(Long userId, LocalDate startOfWeek, LocalDate endOfWeek) {
        return data.stream()
                .filter(schedule -> !schedule.getScheduleDate().isBefore(startOfWeek)
                        && !schedule.getScheduleDate().isAfter(endOfWeek)
                        && userId.equals(schedule.getPersonalSchedule().getUser().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleDomain findById(Long id) {
        return data.stream()
                .filter(schedule -> id.equals(schedule.getId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ScheduleDomain save(ScheduleDomain scheduleDomain) {
        if (scheduleDomain.getId() == null) {
            scheduleDomain = ScheduleDomain.builder()
                    .title(scheduleDomain.getTitle())
                    .content(scheduleDomain.getContent())
                    .scheduleDate(scheduleDomain.getScheduleDate())
                    .startTime(scheduleDomain.getStartTime())
                    .endTime(scheduleDomain.getEndTime())
                    .isComplete(scheduleDomain.isComplete())
                    .type(scheduleDomain.getType())
                    .personalSchedule(scheduleDomain.getPersonalSchedule())
                    .groupSchedule(scheduleDomain.getGroupSchedule())
                    .build();
        } else {
            delete(scheduleDomain);
        }
        data.add(scheduleDomain);
        return scheduleDomain;
    }

    @Override
    public void delete(ScheduleDomain scheduleDomain) {
        data.removeIf(schedule -> schedule.getId().equals(scheduleDomain.getId()));
    }
}