package com.tukorea.planding.domain.planner.dto;

import com.tukorea.planding.domain.planner.PlannerStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PlannerRequest {
    private String title;
    private String content;
    private PlannerStatus status;
    private LocalDateTime deadline;
    private String managerCode;
    private List<String> userCodes;
    private Long scheduleId;
}
