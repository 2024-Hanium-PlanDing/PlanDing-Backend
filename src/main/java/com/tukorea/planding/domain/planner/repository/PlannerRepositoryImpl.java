package com.tukorea.planding.domain.planner.repository;

import com.tukorea.planding.domain.planner.entity.Planner;
import com.tukorea.planding.domain.planner.entity.domain.PlannerDomain;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.domain.ScheduleDomain;
import com.tukorea.planding.global.error.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PlannerRepositoryImpl implements PlannerRepository {

    private final PlannerJpaRepository plannerJpaRepository;

    @Override
    public List<PlannerDomain> findBySchedule(ScheduleDomain scheduleDomain) {
        return plannerJpaRepository.findBySchedule(Schedule.fromModel(scheduleDomain)).stream()
                .map(Planner::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlannerDomain> findAllByGroupAndDateRange(String groupCode, LocalDate startDate, LocalDate endDate) {
        return plannerJpaRepository.findAllByGroupAndDateRange(groupCode, startDate, endDate).stream()
                .map(Planner::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public PlannerDomain save(PlannerDomain plannerDomain) {
        return plannerJpaRepository.save(Planner.fromModel(plannerDomain)).toModel();
    }

    @Override
    public void delete(PlannerDomain plannerDomain) {
        plannerJpaRepository.delete(Planner.fromModel(plannerDomain));
    }

    @Override
    public PlannerDomain findById(Long Id) {
        return plannerJpaRepository.findById(Id)
                .orElseThrow(() -> new IllegalArgumentException("Planner not found with id: " + Id))
                .toModel();
    }
}
