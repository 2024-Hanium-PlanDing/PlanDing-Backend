package com.tukorea.planding.domain.schedule.dto.request.websocket;

import com.tukorea.planding.global.valid.schedule.ValidScheduleTime;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SendDeleteScheduleDTO(
        @NotNull(message = "Group code 필수")
        String groupCode,
        @NotNull(message = "삭제하려는 ScheduleId 필수")
        Long scheduleId,
        @NotNull(message = "User code 필수")
        String userCode
) {
}
