package com.tukorea.planding.domain.group.repository.favorite;

import com.tukorea.planding.domain.group.entity.GroupFavorite;

import java.util.List;

public interface GroupFavoriteRepositoryCustom {
    Long countMyFavoriteGroup(String userCode);

    Boolean existsByUserAndGroupRoom(String userCode,String groupCode);
}
