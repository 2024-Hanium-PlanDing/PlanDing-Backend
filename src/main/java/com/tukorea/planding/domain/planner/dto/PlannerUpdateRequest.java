package com.tukorea.planding.domain.planner.dto;

import com.tukorea.planding.domain.planner.PlannerStatus;

import java.time.LocalDateTime;
import java.util.List;

public record PlannerUpdateRequest(
        Long plannerId,
        String title,
        String content,
        PlannerStatus status,
        LocalDateTime deadline,
        String manager,
        List<String> userCodes,
        Long scheduleId
) {
}
