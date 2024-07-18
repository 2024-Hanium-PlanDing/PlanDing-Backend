package com.tukorea.planding.domain.notify.dto;

import lombok.Builder;

@Builder
public record NotificationSettingResponse(
        boolean personalSchedule,
        boolean groupSchedule
) {
}
