package com.tukorea.planding.domain.group.repository.usergroup;

import com.tukorea.planding.domain.group.entity.UserGroup;
import com.tukorea.planding.domain.group.entity.domain.UserGroupDomain;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserGroupRepositoryImpl implements UserGroupRepository {

    private final UserGroupJpaRepository userGroupJpaRepository;

    @Override
    public boolean existsByGroupRoomAndUser(String groupCode, String userCode) {
        return userGroupJpaRepository.existsByGroupRoomAndUser(groupCode, userCode);
    }

    @Override
    public List<UserDomain> findUserByIsConnectionFalse(Long groupRoomId) {
        return userGroupJpaRepository.findUserByIsConnectionFalse(groupRoomId).stream()
                .map(User::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public UserGroupDomain findUserByGroupId(Long userId, Long groupRoomId) {
        return userGroupJpaRepository.findUserByGroupId(userId, groupRoomId).toModel();
    }

    @Override
    public UserGroupDomain save(UserGroupDomain userGroupDomain) {
        return userGroupJpaRepository.save(UserGroup.fromModel(userGroupDomain)).toModel();
    }

    @Override
    public void delete(UserGroupDomain userGroupDomain) {
        userGroupJpaRepository.delete(UserGroup.fromModel(userGroupDomain));
    }
}
