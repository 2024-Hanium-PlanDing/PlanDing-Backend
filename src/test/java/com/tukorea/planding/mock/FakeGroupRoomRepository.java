package com.tukorea.planding.mock;

import com.tukorea.planding.domain.group.entity.domain.GroupRoomDomain;
import com.tukorea.planding.domain.group.repository.normal.GroupRoomRepository;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeGroupRoomRepository implements GroupRoomRepository {

    private Long count = 0L;
    private final List<GroupRoomDomain> data = new ArrayList<>();

    @Override
    public Optional<GroupRoomDomain> findByGroupCode(String groupCode) {
        return data.stream().filter(item -> item.getGroupCode().equals(groupCode)).findAny();
    }

    @Override
    public boolean existsByGroupCode(String groupCode) {
        return data.stream().anyMatch(item -> item.getGroupCode().equals(groupCode));
    }

    @Override
    public List<GroupRoomDomain> findGroupRoomsByUserId(Long userId, PageRequest request) {
        // 해당 userId와 관련된 그룹룸을 필터링하여 요청된 페이지만큼 반환
        return data.stream()
                .filter(groupRoom -> groupRoom.getUserGroups().stream()
                        .anyMatch(userGroup -> userGroup.getUser().getId().equals(userId)))
                .skip(request.getOffset())
                .limit(request.getPageSize())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GroupRoomDomain> findByGroupId(Long groupId) {
        return data.stream()
                .filter(item -> item.getId().equals(groupId))
                .findAny();
    }

    @Override
    public GroupRoomDomain save(GroupRoomDomain groupRoomDomain) {
        if (groupRoomDomain.getId() == null || groupRoomDomain.getId() == 0) {
            GroupRoomDomain newGroup = groupRoomDomain.builder()
                    .id(count++)
                    .name(groupRoomDomain.getName())
                    .description(groupRoomDomain.getDescription())
                    .groupCode(groupRoomDomain.getGroupCode())
                    .thumbnail(groupRoomDomain.getThumbnail())
                    .createdDate(groupRoomDomain.getCreatedDate())
                    .modifiedDate(groupRoomDomain.getModifiedDate())
                    .owner(groupRoomDomain.getOwner())
                    .alarm(groupRoomDomain.isAlarm())
                    .build();
            data.add(newGroup);
            return newGroup;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), groupRoomDomain.getId()));
            data.add(groupRoomDomain);
            return groupRoomDomain;
        }
    }

    @Override
    public void delete(GroupRoomDomain groupRoomDomain) {
        data.removeIf(item -> Objects.equals(item.getId(), groupRoomDomain.getId()));
    }
}
