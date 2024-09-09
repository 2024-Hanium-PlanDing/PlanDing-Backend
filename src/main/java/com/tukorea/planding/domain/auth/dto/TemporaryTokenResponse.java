package com.tukorea.planding.domain.auth.dto;

import lombok.Builder;

@Builder
public record TemporaryTokenResponse(
        String temporaryToken
) {
}
