package com.tukorea.planding.domain.schedule.dto.response;

import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleType;
import lombok.Builder;

import java.util.Comparator;

@Builder
public record ScheduleResponse(
        ScheduleCommonResponse scheduleCommonResponse,
        ScheduleType type
) {

    public static ScheduleResponse from(Schedule schedule) {
        return ScheduleResponse.builder()
                .scheduleCommonResponse(ScheduleCommonResponse.from(schedule))
                .type(schedule.getType())
                .build();
    }


    public static Comparator<ScheduleResponse> getComparatorByStartTime() {
        return Comparator.comparing(response -> response.scheduleCommonResponse.startTime());
    }
}
