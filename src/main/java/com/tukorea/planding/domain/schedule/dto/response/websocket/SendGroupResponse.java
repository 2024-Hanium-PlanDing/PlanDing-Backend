package com.tukorea.planding.domain.schedule.dto.response.websocket;

import com.tukorea.planding.domain.schedule.dto.response.ScheduleCommonResponse;
import com.tukorea.planding.domain.schedule.entity.Action;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleType;
import lombok.Builder;

@Builder
public record SendGroupResponse(
        ScheduleCommonResponse scheduleCommonResponse,
        ScheduleType type,
        Action action
) {
    public static SendGroupResponse from(Schedule schedule, Action action) {
        return SendGroupResponse.builder()
                .scheduleCommonResponse(ScheduleCommonResponse.from(schedule))
                .type(ScheduleType.GROUP)
                .action(action)
                .build();
    }
}
