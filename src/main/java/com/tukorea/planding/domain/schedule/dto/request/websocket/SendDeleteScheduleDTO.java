package com.tukorea.planding.domain.schedule.dto.request.websocket;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SendDeleteScheduleDTO(
        @NotNull(message = "삭제하려는 ScheduleId 필수")
        Long scheduleId
) {
}
