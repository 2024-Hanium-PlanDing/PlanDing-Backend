package com.tukorea.planding.domain.planner.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.planner.dto.GroupPlannerResponse;
import com.tukorea.planding.domain.planner.dto.PlannerDeleteRequest;
import com.tukorea.planding.domain.planner.dto.PlannerRequest;
import com.tukorea.planding.domain.planner.dto.PlannerUpdateRequest;
import com.tukorea.planding.domain.planner.service.GroupPlannerService;
import com.tukorea.planding.domain.user.dto.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "GroupPlanner", description = "그룹플래너")
@RestController
@RequiredArgsConstructor
public class GroupPlannerController {

    private final GroupPlannerService groupPlannerService;

    @MessageMapping("/planner/create/{groupCode}")
    @SendTo("/sub/schedule/{groupCode}")
    public CommonResponse<?> createPlanner(StompHeaderAccessor accessor, @DestinationVariable String groupCode, PlannerRequest request) {
        String userCode = accessor.getSessionAttributes().get("userCode").toString();
        return CommonUtils.success(groupPlannerService.createGroupPlanner(userCode, request, groupCode));
    }

    @MessageMapping("/planner/delete/{groupCode}")
    @SendTo("/sub/schedule/{groupCode}")
    public CommonResponse<?> deletePlanner(StompHeaderAccessor accessor, @DestinationVariable String groupCode, PlannerDeleteRequest request) {
        String userCode = accessor.getSessionAttributes().get("userCode").toString();
        return CommonUtils.success(groupPlannerService.deletePlanner(userCode, groupCode, request.plannerId()));
    }

    @MessageMapping("/planner/update/{groupCode}")
    @SendTo("/sub/schedule/{groupCode}")
    public CommonResponse<?> updatePlanner(StompHeaderAccessor accessor, @DestinationVariable String groupCode, PlannerUpdateRequest request) {
        String userCode = accessor.getSessionAttributes().get("userCode").toString();
        return CommonUtils.success(groupPlannerService.updateGroupPlanner(userCode, groupCode, request));
    }

    @Operation(summary = "그룹 스케줄 플래너: 조회")
    @GetMapping("/api/v1/group-rooms/{groupCode}/planner/{scheduleId}")
    public CommonResponse<List<GroupPlannerResponse>> getPlannersByGroupRoom(@AuthenticationPrincipal UserInfo userInfo, @PathVariable String groupCode, @PathVariable Long scheduleId) {
        return CommonUtils.success(groupPlannerService.getPlannersByGroupRoom(userInfo, groupCode, scheduleId));
    }
}
