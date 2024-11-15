package com.tukorea.planding.mock;

import com.tukorea.planding.domain.group.entity.domain.GroupFavoriteDomain;
import com.tukorea.planding.domain.group.repository.favorite.GroupFavoriteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FakeFavoriteGroupRepository implements GroupFavoriteRepository {

    long count = 0;
    List<GroupFavoriteDomain> data = new ArrayList<>();

    @Override
    public GroupFavoriteDomain save(GroupFavoriteDomain groupFavoriteDomain) {
        if (groupFavoriteDomain.getId() == null || groupFavoriteDomain.getId() == 0) {
            GroupFavoriteDomain newData = GroupFavoriteDomain.builder()
                    .id(count++)
                    .groupRoomDomain(groupFavoriteDomain.getGroupRoomDomain())
                    .userDomain(groupFavoriteDomain.getUserDomain())
                    .build();
            data.add(newData);
            return newData;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), groupFavoriteDomain.getId()));
            data.add(groupFavoriteDomain);
            return groupFavoriteDomain;
        }
    }

    @Override
    public void deleteByUserIdAndGroupRoomId(Long userId, String groupCode) {
        data.removeIf(item -> item.getUserDomain().getId().equals(userId) && item.getGroupRoomDomain().getGroupCode().equals(groupCode));
    }

    @Override
    public List<GroupFavoriteDomain> findFavoriteGroupsByUser(Long userId) {
        return data.stream()
                .filter(item -> item.getUserDomain().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean existsByUserAndGroupRoom(String userCode, String groupCode) {
        return data.stream()
                .anyMatch(item -> item.getUserDomain().getUserCode().equals(userCode) && item.getGroupRoomDomain().getGroupCode().equals(groupCode));
    }
}
