package com.tukorea.planding.domain.notify.dto;

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
