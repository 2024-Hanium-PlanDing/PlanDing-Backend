package com.tukorea.planding.domain.group.repository.normal;

import com.tukorea.planding.domain.group.entity.domain.GroupRoomDomain;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface GroupRoomRepository {
    Optional<GroupRoomDomain> findByGroupCode(String groupCode);

    boolean existsByGroupCode(String groupCode);

    List<GroupRoomDomain> findGroupRoomsByUserId(Long userId, PageRequest request);

    Optional<GroupRoomDomain> findByGroupId(Long groupId);

    GroupRoomDomain save(GroupRoomDomain groupRoomDomain);

    void delete(GroupRoomDomain groupRoomDomain);
}
