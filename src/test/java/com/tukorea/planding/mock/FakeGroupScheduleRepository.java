package com.tukorea.planding.mock;

import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.domain.GroupScheduleDomain;
import com.tukorea.planding.domain.schedule.repository.port.GroupScheduleRepository;
import com.tukorea.planding.domain.user.entity.UserDomain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FakeGroupScheduleRepository implements GroupScheduleRepository {

    long count = 0;
    List<GroupScheduleDomain> data = new ArrayList<>();

    @Override
    public List<GroupScheduleDomain> findAllByGroupRoomId(Long groupId) {
        return data.stream().filter(item -> item.getId().equals(groupId)).collect(Collectors.toList());
    }

    @Override
    public GroupScheduleDomain save(GroupScheduleDomain groupScheduleDomain) {
        if (groupScheduleDomain.getId() == null || groupScheduleDomain.getId() == 0) {
            GroupScheduleDomain newGroupSchedule = GroupScheduleDomain.builder()
                    .id(groupScheduleDomain.getId())
                    .schedules(groupScheduleDomain.getSchedules())
                    .groupRoom(groupScheduleDomain.getGroupRoom())
                    .build();
            data.add(newGroupSchedule);
            return newGroupSchedule;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), groupScheduleDomain.getId()));
            data.add(groupScheduleDomain);
            return groupScheduleDomain;
        }
    }
}
