package com.tukorea.planding.domain.planner.controller;

import com.tukorea.planding.common.CommonResponse;
import com.tukorea.planding.common.CommonUtils;
import com.tukorea.planding.domain.planner.dto.PlannerRequest;
import com.tukorea.planding.domain.planner.dto.personal.PersonalPlannerResponse;
import com.tukorea.planding.domain.planner.service.PersonalPlannerService;
import com.tukorea.planding.domain.user.dto.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PersonalPlanner", description = "개인 플래너")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/planner")
public class PersonalPlannerController {

    private final PersonalPlannerService plannerService;

    @Operation(summary = "개인 플래너: 생성")
    @PostMapping("/personal")
    public CommonResponse<PersonalPlannerResponse> createPersonalPlanner(
            @AuthenticationPrincipal UserInfo userInfo,
            @RequestBody PlannerRequest request) {
        return CommonUtils.success(plannerService.createPersonalPlanner(userInfo, request));
    }

    @Operation(summary = "개인 플래너:삭제")
    @DeleteMapping("/{plannerId}")
    public CommonResponse<?> deletePlanner(@AuthenticationPrincipal UserInfo userInfo, @PathVariable Long plannerId) {
        plannerService.deletePlanner(userInfo, plannerId);
        return CommonUtils.successWithEmptyData();
    }

    @Operation(summary = "개인 플래너:수정")
    @PatchMapping("/{plannerId}")
    public CommonResponse<PersonalPlannerResponse> updatePlanner(@AuthenticationPrincipal UserInfo userInfo, @RequestBody PlannerRequest request, @PathVariable Long plannerId) {
        return CommonUtils.success(plannerService.updatePlanner(userInfo, request, plannerId));
    }


}
