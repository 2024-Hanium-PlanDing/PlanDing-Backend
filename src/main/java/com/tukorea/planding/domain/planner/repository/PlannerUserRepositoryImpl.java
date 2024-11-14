package com.tukorea.planding.domain.planner.repository;

import com.tukorea.planding.domain.planner.entity.Planner;
import com.tukorea.planding.domain.planner.entity.PlannerUser;
import com.tukorea.planding.domain.planner.entity.domain.PlannerDomain;
import com.tukorea.planding.domain.planner.entity.domain.PlannerUserDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlannerUserRepositoryImpl implements PlannerUserRepository {
    private final PlannerUserJpaRepository plannerUserJpaRepository;

    @Override
    public void deleteByPlanner(PlannerDomain plannerDomain) {
        plannerUserJpaRepository.deleteByPlanner(Planner.fromModel(plannerDomain));
    }

    @Override
    public void delete(PlannerUserDomain plannerUserDomain) {
        plannerUserJpaRepository.deleteByPlanner(Planner.fromModel(plannerUserDomain.getPlanner()));
    }

    @Override
    public PlannerUserDomain save(PlannerUserDomain plannerUserDomain) {
        return plannerUserJpaRepository.save(PlannerUser.fromModel(plannerUserDomain)).toModel();
    }
}
