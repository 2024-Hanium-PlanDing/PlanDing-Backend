package com.tukorea.planding.domain.notify.dto.alarm;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DailyNotificationDto(
        String userCode,
        String message,
        String url,
        LocalDate date
) {
}
