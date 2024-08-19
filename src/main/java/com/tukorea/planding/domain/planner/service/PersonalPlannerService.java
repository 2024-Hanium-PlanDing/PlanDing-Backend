package com.tukorea.planding.domain.planner.service;

import com.tukorea.planding.domain.planner.PlannerRole;
import com.tukorea.planding.domain.planner.dto.PlannerRequest;
import com.tukorea.planding.domain.planner.dto.personal.PersonalPlannerResponse;
import com.tukorea.planding.domain.planner.entity.Planner;
import com.tukorea.planding.domain.planner.entity.PlannerUser;
import com.tukorea.planding.domain.planner.repository.PlannerRepository;
import com.tukorea.planding.domain.planner.repository.PlannerUserRepository;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.service.ScheduleQueryService;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonalPlannerService {

    private final PlannerRepository plannerRepository;
    private final UserQueryService userQueryService;
    private final PlannerUserRepository plannerUserRepository;
    private final ScheduleQueryService scheduleQueryService;

    public PersonalPlannerResponse createPersonalPlanner(UserInfo userInfo, PlannerRequest request) {
        Schedule schedule = scheduleQueryService.findScheduleById(request.getScheduleId());

        Planner planner = Planner.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .deadline(request.getDeadline())
                .status(request.getStatus())
                .schedule(schedule)
                .build();

        Planner savedPlanner = plannerRepository.save(planner);
        savedPlanner.generatePlannerNumber();

        User manager = userQueryService.getUserByUserCode(request.getManagerCode());
        planner.getUsers().add(assignUserToPlanner(planner, manager, PlannerRole.MANAGER));

        return PersonalPlannerResponse.fromEntity(planner);

    }

    private PlannerUser assignUserToPlanner(Planner planner, User user, PlannerRole role) {
        PlannerUser plannerUser = PlannerUser.builder()
                .planner(planner)
                .user(user)
                .role(role)
                .build();
        return plannerUserRepository.save(plannerUser);
    }

    public void deletePlanner(UserInfo userInfo, Long plannerId) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("Planner not found with id: " + plannerId));

        // 관련된 PlannerUser 엔티티 삭제
        plannerUserRepository.deleteByPlanner(planner);

        // 플래너 삭제
        plannerRepository.delete(planner);

        // 플래너 번호 재조정
        CompletableFuture.runAsync(() -> adjustPlannerNumbers(planner.getSchedule(), planner.getPlannerNumber()));

    }

    public PersonalPlannerResponse updatePlanner(UserInfo userInfo, PlannerRequest request, Long plannerId) {

        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("Planner not found with id: " + plannerId));

        planner.update(request.getTitle(), request.getContent(), request.getStatus(), request.getDeadline());

        return PersonalPlannerResponse.fromEntity(planner);
    }

    private void adjustPlannerNumbers(Schedule schedule, int deletedPlannerNumber) {
        List<Planner> planners = schedule.getPlanners().stream()
                .filter(planner -> planner.getPlannerNumber() > deletedPlannerNumber)
                .collect(Collectors.toList());

        // 삭제된 플래너 번호 이후의 모든 플래너 번호를 -1 조정
        for (Planner planner : planners) {
            planner.declinePlannerNumber();
            plannerRepository.save(planner);
        }
    }

}
