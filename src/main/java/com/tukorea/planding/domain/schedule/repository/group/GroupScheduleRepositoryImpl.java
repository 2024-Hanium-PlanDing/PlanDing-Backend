package com.tukorea.planding.domain.schedule.repository.group;

import com.tukorea.planding.domain.schedule.entity.GroupSchedule;
import com.tukorea.planding.domain.schedule.entity.domain.GroupScheduleDomain;
import com.tukorea.planding.domain.schedule.repository.port.GroupScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GroupScheduleRepositoryImpl implements GroupScheduleRepository {

    private final GroupScheduleJpaRepository groupScheduleJpaRepository;

    @Override
    public List<GroupScheduleDomain> findAllByGroupRoomId(Long groupId) {
        return groupScheduleJpaRepository.findAllByGroupRoomId(groupId).stream()
                .map(GroupSchedule::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public GroupScheduleDomain save(GroupScheduleDomain groupScheduleDomain) {
        return groupScheduleJpaRepository.save(GroupSchedule.fromModel(groupScheduleDomain)).toModel();
    }
}
