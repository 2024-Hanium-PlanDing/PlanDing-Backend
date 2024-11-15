package com.tukorea.planding.domain.group.service;

import com.tukorea.planding.domain.group.dto.response.GroupFavoriteResponse;
import com.tukorea.planding.domain.group.dto.response.GroupResponse;
import com.tukorea.planding.domain.group.entity.domain.GroupFavoriteDomain;
import com.tukorea.planding.domain.group.entity.domain.GroupRoomDomain;
import com.tukorea.planding.domain.group.repository.favorite.GroupFavoriteRepository;
import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.user.dto.UserResponse;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.domain.user.service.UserQueryService;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Builder
@Transactional
@RequiredArgsConstructor
public class GroupFavoriteService {

    private final GroupFavoriteRepository groupFavoriteRepository;
    private final UserQueryService userQueryService;
    private final GroupQueryService groupQueryService;


    @Transactional(readOnly = true)
    public List<GroupResponse> findFavoriteGroupsByUser(UserResponse userResponse) {
        return groupFavoriteRepository.findFavoriteGroupsByUser(userResponse.getId())
                .stream()
                .map(GroupFavoriteDomain::getGroupRoomDomain)
                .map(GroupResponse::toGroupResponse)
                .collect(Collectors.toList());
    }

    public GroupFavoriteResponse addFavorite(UserResponse userResponse, String groupCode) {
        log.info("Add 그룹 즐겨찾기 for userCodes {} and group {}", userResponse.getUserCode(), groupCode);
        if (groupFavoriteRepository.existsByUserAndGroupRoom(userResponse.getUserCode(), groupCode)) {
            throw new BusinessException(ErrorCode.FAVORITE_ALREADY_ADD);
        }

        UserDomain user = userQueryService.getUserByUserCode(userResponse.getUserCode());
        GroupRoomDomain groupRoom = groupQueryService.getGroupByCode(groupCode);

        GroupFavoriteDomain groupFavorite = GroupFavoriteDomain.createGroupFavorite(user, groupRoom);
        GroupFavoriteDomain save = groupFavoriteRepository.save(groupFavorite);
        log.info("그룹 즐겨찾기 등록완료  for userCodes {} and group {}", userResponse.getUserCode(), groupCode);
        return GroupFavoriteResponse.from(save);
    }

    public void deleteFavorite(UserResponse userResponse, String groupCode) {
        log.info("Deleting 그룹 즐겨찾기 for userCodes {} and group {}", userResponse.getUserCode(), groupCode);
        try {
            groupFavoriteRepository.deleteByUserIdAndGroupRoomId(userResponse.getId(), groupCode);
        } catch (EmptyResultDataAccessException e) {
            throw new BusinessException(ErrorCode.FAVORITE_ALREADY_DELETE);
        }
    }
}
