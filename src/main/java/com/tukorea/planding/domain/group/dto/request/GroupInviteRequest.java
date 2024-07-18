package com.tukorea.planding.domain.group.dto.request;

import lombok.Builder;

@Builder
public record GroupInviteRequest(
        Long groupId,
        String userCode
) {

}