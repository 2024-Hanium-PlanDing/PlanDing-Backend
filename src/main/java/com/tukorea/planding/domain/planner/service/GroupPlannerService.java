package com.tukorea.planding.domain.planner.service;

import com.tukorea.planding.domain.group.service.query.UserGroupQueryService;
import com.tukorea.planding.domain.notify.entity.NotificationType;
import com.tukorea.planding.domain.planner.PlannerRole;
import com.tukorea.planding.domain.planner.dto.PlannerRequest;
import com.tukorea.planding.domain.planner.dto.GroupPlannerResponse;
import com.tukorea.planding.domain.planner.dto.PlannerResponse;
import com.tukorea.planding.domain.planner.dto.PlannerUpdateRequest;
import com.tukorea.planding.domain.planner.dto.group.PlannerWeekResponse;
import com.tukorea.planding.domain.planner.dto.SchedulePlannerResponse;
import com.tukorea.planding.domain.planner.entity.Planner;
import com.tukorea.planding.domain.planner.entity.PlannerUser;
import com.tukorea.planding.domain.planner.repository.PlannerRepository;
import com.tukorea.planding.domain.planner.repository.PlannerUserRepository;
import com.tukorea.planding.domain.schedule.entity.Action;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.service.ScheduleQueryService;
import com.tukorea.planding.domain.user.dto.UserResponse;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.domain.user.service.UserQueryService;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupPlannerService {

    private final PlannerRepository plannerRepository;
    private final UserQueryService userQueryService;
    private final PlannerUserRepository plannerUserRepository;
    private final UserGroupQueryService userGroupQueryService;
    private final ScheduleQueryService scheduleQueryService;

    public GroupPlannerResponse createGroupPlanner(String userCode, PlannerRequest request, String groupCode) {
        if (!userGroupQueryService.checkUserAccessToGroupRoom(groupCode, userCode)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

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

        if (!request.getUserCodes().isEmpty()) {
            request.getUserCodes().stream()
                    .map(userQueryService::getUserByUserCode) // 유저를 조회
                    .forEach(user -> planner.getUsers().add(assignUserToPlanner(planner, User.fromModel(user), PlannerRole.GENERAL))); // 유저를 플래너에 할당

        }

        UserDomain manager = userQueryService.getUserByUserCode(request.getManagerCode());
        planner.getUsers().add(assignUserToPlanner(planner, User.fromModel(manager), PlannerRole.MANAGER));

        return GroupPlannerResponse.fromEntity(planner, Action.CREATE);

    }

    private PlannerUser assignUserToPlanner(Planner planner, User user, PlannerRole role) {
        PlannerUser plannerUser = PlannerUser.builder()
                .planner(planner)
                .user(user)
                .role(role)
                .build();
        return plannerUserRepository.save(plannerUser);
    }

    public GroupPlannerResponse deletePlanner(String userCode, String groupCode, Long plannerId) {
        if (!userGroupQueryService.checkUserAccessToGroupRoom(groupCode, userCode)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("Planner not found with id: " + plannerId));

        // 관련된 PlannerUser 엔티티 삭제
        plannerUserRepository.deleteByPlanner(planner);

        // 플래너 삭제
        plannerRepository.delete(planner);

        // 플래너 번호 재조정
        CompletableFuture.runAsync(() -> adjustPlannerNumbers(planner.getSchedule(), planner.getPlannerNumber()));

        return GroupPlannerResponse.delete(planner);
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

    public PlannerResponse getPlannerByGroup(UserResponse userResponse, String groupCode, Long plannerId) {
        if (!userGroupQueryService.checkUserAccessToGroupRoom(groupCode, userResponse.getUserCode())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new IllegalArgumentException("Planner not found with id: " + plannerId));

        return PlannerResponse.fromEntity(planner);
    }

    public GroupPlannerResponse updateGroupPlanner(String userCode, String groupCode, PlannerUpdateRequest request) {
        if (!userGroupQueryService.checkUserAccessToGroupRoom(groupCode, userCode)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        Planner planner = plannerRepository.findById(request.plannerId())
                .orElseThrow(() -> new IllegalArgumentException("Planner not found with id: " + request.plannerId()));

        UserDomain newManager = userQueryService.getUserByUserCode(request.managerCode());
        planner.updateManager(User.fromModel(newManager));

        updateUsers(planner, request.userCodes());

        planner.update(request.title(), request.content(), request.status(), request.deadline());

        return GroupPlannerResponse.fromEntity(planner, Action.UPDATE);
    }

    private void updateUsers(Planner planner, List<String> newUserCodes) {
        List<PlannerUser> currentUsers = planner.getUsers().stream()
                .filter(plannerUser -> plannerUser.getRole() == PlannerRole.GENERAL)
                .collect(Collectors.toList());

        // 기존 사용자를 제거
        currentUsers.removeIf(plannerUser -> {
            if (!newUserCodes.contains(plannerUser.getUser().getUserCode())) {
                plannerUserRepository.delete(plannerUser);
                return true;
            }
            return false;
        });

        // 새로운 사용자를 추가
        for (String userCode : newUserCodes) {
            UserDomain user = userQueryService.getUserByUserCode(userCode);
            if (currentUsers.stream().noneMatch(plannerUser -> plannerUser.getUser().equals(user))) {
                assignUserToPlanner(planner, User.fromModel(user), PlannerRole.GENERAL);
            }
        }
    }

    public SchedulePlannerResponse getPlannersByGroup(UserResponse userResponse, String groupCode, Long scheduleId) {
        if (!userGroupQueryService.checkUserAccessToGroupRoom(groupCode, userResponse.getUserCode())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        Schedule schedule = scheduleQueryService.findScheduleById(scheduleId);
        List<Planner> planners = plannerRepository.findBySchedule(schedule);

        return SchedulePlannerResponse.fromEntity(planners, schedule);
    }

    public List<PlannerWeekResponse> findPlannersByGroupCodeAndDateRange(LocalDate startDate, LocalDate endDate, String groupCode, UserResponse userResponse) {
        List<Planner> planners = plannerRepository.findAllByGroupAndDateRange(groupCode, startDate, endDate);

        Map<Long, List<Planner>> groupedPlanners = planners.stream().collect(Collectors.groupingBy(planner -> planner.getSchedule().getId()));

        return groupedPlanners.entrySet().stream()
                .map(entry -> PlannerWeekResponse.builder()
                        .scheduleId(entry.getKey())
                        .scheduleTitle(entry.getValue().get(0).getSchedule().getTitle())
                        .scheduleDate(entry.getValue().get(0).getSchedule().getScheduleDate())
                        .planners(entry.getValue().stream()
                                .map(PlannerResponse::fromEntity)
                                .collect(Collectors.toList()))
                        .type(NotificationType.PLANNER)
                        .build())
                .collect(Collectors.toList());
    }
}
