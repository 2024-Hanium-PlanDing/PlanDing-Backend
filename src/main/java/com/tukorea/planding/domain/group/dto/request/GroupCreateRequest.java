package com.tukorea.planding.domain.group.dto.request;

import lombok.Builder;

@Builder
public record GroupCreateRequest(
        String name,
        String description
) {
}
