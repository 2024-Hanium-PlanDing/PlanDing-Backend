package com.tukorea.planding.domain.notify.dto.alarm;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DailyNotificationDto(
        String userCode,
        Long personal,
        Long group,
        String url,
        LocalDate date
) {
}
