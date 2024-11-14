package com.tukorea.planding.domain.schedule.repository.port;

import com.tukorea.planding.domain.schedule.entity.GroupSchedule;
import com.tukorea.planding.domain.schedule.entity.domain.GroupScheduleDomain;

import java.util.List;

public interface GroupScheduleRepository {
    List<GroupScheduleDomain> findAllByGroupRoomId(Long groupId);

    GroupScheduleDomain save(GroupScheduleDomain groupScheduleDomain);

}
