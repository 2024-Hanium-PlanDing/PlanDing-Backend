package com.tukorea.planding.domain.group.dto.response;

import com.tukorea.planding.domain.group.entity.domain.GroupRoomDomain;
import lombok.Builder;

@Builder
public record GroupResponse(
        Long id,
        String name,
        String description,
        String code,
        String ownerCode,
        String thumbnailPath,
        boolean alarm
) {
    public static GroupResponse toGroupResponse(GroupRoomDomain groupRoomDomain) {
        return new GroupResponse(groupRoomDomain.getId(), groupRoomDomain.getName(), groupRoomDomain.getDescription(), groupRoomDomain.getGroupCode(), groupRoomDomain.getOwner().getUserCode(), groupRoomDomain.getThumbnail(), groupRoomDomain.isAlarm());
    }

}
