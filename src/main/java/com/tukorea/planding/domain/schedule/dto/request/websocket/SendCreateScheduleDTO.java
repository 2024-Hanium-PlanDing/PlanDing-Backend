package com.tukorea.planding.domain.schedule.dto.request.websocket;

import com.tukorea.planding.global.valid.schedule.ValidScheduleTime;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@ValidScheduleTime
public record SendCreateScheduleDTO(
        String title,
        String content,
        LocalDate scheduleDate,
        Integer startTime,
        Integer endTime
) {
}
