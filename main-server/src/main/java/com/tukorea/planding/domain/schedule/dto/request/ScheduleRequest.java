package com.tukorea.planding.domain.schedule.dto.request;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ScheduleRequest(
        String userCode,
        String title,
        String content,
        LocalDate scheduleDate,
        Integer startTime,
        Integer endTime
) {

}
