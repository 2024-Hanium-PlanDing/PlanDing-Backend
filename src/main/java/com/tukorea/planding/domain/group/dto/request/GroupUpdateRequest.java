package com.tukorea.planding.domain.group.dto.request;

import lombok.Builder;

@Builder
public record GroupUpdateRequest(
        Long groupId,
        String name,
        String description
) {
}
