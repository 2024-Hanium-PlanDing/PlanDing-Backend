package com.tukorea.planding.domain.group.repository.favorite;

import com.tukorea.planding.domain.group.entity.GroupFavorite;
import com.tukorea.planding.domain.group.entity.domain.GroupFavoriteDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GroupFavoriteRepositoryImpl implements GroupFavoriteRepository {

    private final GroupFavoriteJpaRepository groupFavoriteJpaRepository;

    @Override
    public GroupFavoriteDomain save(GroupFavoriteDomain groupFavoriteDomain) {
        return groupFavoriteJpaRepository.save(GroupFavorite.fromModel(groupFavoriteDomain)).toModel();
    }

    @Override
    public void deleteByUserIdAndGroupRoomId(Long userId, String groupCode) {
        groupFavoriteJpaRepository.deleteByUserIdAndGroupRoomId(userId, groupCode);
    }

    @Override
    public List<GroupFavoriteDomain> findFavoriteGroupsByUser(Long userId) {
        return groupFavoriteJpaRepository.findFavoriteGroupsByUser(userId).stream()
                .map(GroupFavorite::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean existsByUserAndGroupRoom(String userCode, String groupCode) {
        return null;
    }
}
