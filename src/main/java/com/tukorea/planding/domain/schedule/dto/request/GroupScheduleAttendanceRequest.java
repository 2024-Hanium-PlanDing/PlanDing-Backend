package com.tukorea.planding.domain.schedule.dto.request;

import com.tukorea.planding.domain.schedule.entity.ScheduleStatus;
import lombok.Builder;

@Builder
public record GroupScheduleAttendanceRequest(
        ScheduleStatus status,
        Long scheduleId
) {
}
