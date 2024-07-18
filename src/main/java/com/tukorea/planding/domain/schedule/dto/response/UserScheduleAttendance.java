package com.tukorea.planding.domain.schedule.dto.response;

import com.tukorea.planding.domain.schedule.entity.ScheduleStatus;
import lombok.Builder;

@Builder
public record UserScheduleAttendance(
        String userCode,
        String userName,
        ScheduleStatus status
) {

}
