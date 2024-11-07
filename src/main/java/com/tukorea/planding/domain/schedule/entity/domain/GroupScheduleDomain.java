package com.tukorea.planding.domain.schedule.entity.domain;

import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.entity.domain.GroupRoomDomain;
import com.tukorea.planding.domain.schedule.entity.Schedule;

import java.util.HashSet;
import java.util.Set;

public class GroupScheduleDomain {

        private Long id;

        private GroupRoomDomain groupRoom;

        private Set<ScheduleDomain> schedules = new HashSet<>();

}
