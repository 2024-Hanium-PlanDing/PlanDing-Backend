package com.tukorea.planding.domain.group.dto.request;

public record GroupUpdateRequest(
        Long groupId,
        String name,
        String description
) {
}
