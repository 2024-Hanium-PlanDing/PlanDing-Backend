package com.tukorea.planding.domain.schedule.dto.request.websocket;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SendUpdateScheduleDTO(
        Long scheduleId,
        String title,
        String content,
        LocalDate scheduleDate,
        Integer startTime,
        Integer endTime
) {
}
