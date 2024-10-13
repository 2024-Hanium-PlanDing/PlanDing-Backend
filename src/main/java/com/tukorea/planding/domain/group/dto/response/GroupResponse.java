package com.tukorea.planding.domain.group.dto.response;

import com.tukorea.planding.domain.group.entity.GroupRoom;
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
    public static GroupResponse from(GroupRoom groupRoom) {
        return new GroupResponse(groupRoom.getId(), groupRoom.getName(), groupRoom.getDescription(), groupRoom.getGroupCode(), groupRoom.getOwner().getUserCode(), groupRoom.getThumbnail(),groupRoom.isAlarm());
    }
}
