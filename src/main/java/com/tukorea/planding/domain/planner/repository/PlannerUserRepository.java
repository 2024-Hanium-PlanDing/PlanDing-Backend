package com.tukorea.planding.domain.planner.repository;

import com.tukorea.planding.domain.planner.entity.Planner;
import com.tukorea.planding.domain.planner.entity.PlannerUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlannerUserRepository extends JpaRepository<PlannerUser, Long> {

    void deleteByPlanner(Planner planner);
}
