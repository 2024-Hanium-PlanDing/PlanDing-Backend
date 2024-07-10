package com.tukorea.planding.domain.group.repository.normal;

import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.user.entity.User;

import java.util.List;

public interface GroupRoomRepositoryCustom {
    List<GroupRoom> findGroupRoomsByUserId(Long userId);

    List<User> getGroupUsers(String groupCode);
}
