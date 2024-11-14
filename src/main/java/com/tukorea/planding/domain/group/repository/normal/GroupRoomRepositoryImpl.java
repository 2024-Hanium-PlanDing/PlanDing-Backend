package com.tukorea.planding.domain.group.repository.normal;

import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.entity.domain.GroupRoomDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class GroupRoomRepositoryImpl implements GroupRoomRepository {

    private final GroupRoomJpaRepository groupRoomJpaRepository;

    @Override
    public Optional<GroupRoomDomain> findByGroupCode(String groupCode) {
        return groupRoomJpaRepository.findByGroupCode(groupCode).map(GroupRoom::toModel);
    }

    @Override
    public boolean existsByGroupCode(String groupCode) {
        return groupRoomJpaRepository.existsByGroupCode(groupCode);
    }

    @Override
    public List<GroupRoomDomain> findGroupRoomsByUserId(Long userId, PageRequest page) {
        return groupRoomJpaRepository.findGroupRoomsByUserId(userId, page).stream()
                .map(GroupRoom::toModel)
                .collect(Collectors.toList());
    }


    @Override
    public Optional<GroupRoomDomain> findByGroupId(Long groupId) {
        return groupRoomJpaRepository.findById(groupId).map(GroupRoom::toModel);
    }

    @Override
    public GroupRoomDomain save(GroupRoomDomain groupRoomDomain) {
        return groupRoomJpaRepository.save(GroupRoom.fromModel(groupRoomDomain)).toModel();
    }

    @Override
    public void delete(GroupRoomDomain groupRoomDomain) {
        groupRoomJpaRepository.delete(GroupRoom.fromModel(groupRoomDomain));
    }
}
