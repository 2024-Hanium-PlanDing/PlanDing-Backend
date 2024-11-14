package com.tukorea.planding.domain.schedule.entity.domain;

import com.tukorea.planding.domain.group.entity.domain.GroupRoomDomain;
import lombok.Builder;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class GroupScheduleDomain {

    private final Long id;
    private final GroupRoomDomain groupRoom;
    private final Set<ScheduleDomain> schedules;

    @Builder
    public GroupScheduleDomain(Long id, GroupRoomDomain groupRoom, Set<ScheduleDomain> schedules) {
        this.id = id;
        this.groupRoom = groupRoom;
        this.schedules = schedules;
    }
}
