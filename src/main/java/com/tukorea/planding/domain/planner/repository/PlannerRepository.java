package com.tukorea.planding.domain.planner.repository;

import com.tukorea.planding.domain.planner.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlannerRepository extends JpaRepository<Planner, Long> {
}
