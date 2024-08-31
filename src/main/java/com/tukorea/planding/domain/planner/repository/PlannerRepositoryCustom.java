package com.tukorea.planding.domain.planner.repository;

import com.tukorea.planding.domain.planner.entity.Planner;

import java.time.LocalDate;
import java.util.List;

public interface PlannerRepositoryCustom {
    List<Planner> findPlannersByGroupCodeAndDateRange(String groupCode, LocalDate startDate, LocalDate endDate);
}
