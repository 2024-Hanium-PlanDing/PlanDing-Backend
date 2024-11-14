package com.tukorea.planding.domain.group.repository.favorite;

import com.tukorea.planding.domain.group.entity.domain.GroupFavoriteDomain;

import java.util.List;

public interface GroupFavoriteRepository {

    GroupFavoriteDomain save(GroupFavoriteDomain groupFavoriteDomain);
    void deleteByUserIdAndGroupRoomId( Long userId,  String groupCode);

    List<GroupFavoriteDomain> findFavoriteGroupsByUser(Long userId);

    Boolean existsByUserAndGroupRoom(String userCode,String groupCode);

}
