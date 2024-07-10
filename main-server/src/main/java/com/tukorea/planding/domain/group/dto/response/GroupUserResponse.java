package com.tukorea.planding.domain.group.dto.response;

import com.tukorea.planding.domain.user.dto.UserInfoSimple;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record GroupUserResponse(
        String id,
        String name,
        String groupCode,
        String description,
        String thumbnailUrl,
        LocalDate createdBy,
        List<UserInfoSimple> users
) {

}
