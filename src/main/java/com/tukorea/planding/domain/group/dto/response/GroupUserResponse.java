package com.tukorea.planding.domain.group.dto.response;

import com.tukorea.planding.domain.group.entity.UserGroup;
import com.tukorea.planding.domain.schedule.dto.response.GroupScheduleResponse;
import com.tukorea.planding.domain.schedule.dto.response.SimpleGroupScheduleResponse;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.dto.UserInfoSimple;
import com.tukorea.planding.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Builder
public record GroupUserResponse(
        String id,
        String name,
        String description,
        String thumbnailUrl,
        LocalDate createdBy,
        List<SimpleGroupScheduleResponse> groupScheduleResponses,
        List<UserInfoSimple> users
) {

}
