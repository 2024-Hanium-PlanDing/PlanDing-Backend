package com.tukorea.planding.domain.group.dto.response;

import com.tukorea.planding.domain.user.dto.UserInfoSimple;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record GroupInformationResponse(
        Long id,
        String owner,
        String name,
        String groupCode,
        String description,
        String thumbnailUrl,
        LocalDate createdBy,
        boolean isFavorite,
        boolean isAlarm,
        List<UserInfoSimple> users,
        boolean isGroupAdmin
) {


}
