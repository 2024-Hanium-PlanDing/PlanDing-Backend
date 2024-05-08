package com.tukorea.planding.domain.group.repository;

import com.tukorea.planding.domain.group.entity.GroupFavorite;

import java.util.List;

public interface GroupFavoriteRepositoryCustom {
    Long countMyFavoriteGroup(String userCode);

    Boolean existsByUserAndGroupRoom(String userCode);

    GroupFavorite findByUserIdAndGroupId(Long userId, Long groupRoomId);

    List<GroupFavorite> findByUserFavorite(Long userId);
}
