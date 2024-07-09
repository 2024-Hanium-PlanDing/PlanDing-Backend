package com.tukorea.planding.domain.schedule.dto.request;

import com.tukorea.planding.domain.schedule.entity.Action;
import com.tukorea.planding.global.valid.schedule.ValidScheduleTime;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@ValidScheduleTime
public record GroupScheduleRequest(
        String groupCode,
        Long scheduleId,
        String userCode,
        String title,
        String content,
        LocalDate scheduleDate,
        Integer startTime,
        Integer endTime,
        @NotNull(message = "Action 타입을 입력해 주세요.")
        Action action
) {

}
