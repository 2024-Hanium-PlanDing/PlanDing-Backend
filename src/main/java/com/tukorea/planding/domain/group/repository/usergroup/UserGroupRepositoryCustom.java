package com.tukorea.planding.domain.group.repository.usergroup;

import com.tukorea.planding.domain.group.entity.UserGroup;
import com.tukorea.planding.domain.user.entity.User;

import java.util.List;

public interface UserGroupRepositoryCustom {
    boolean existsByGroupRoomIdAndUserId(String groupCode, String userCode);

    List<User> findUserByIsConnectionFalse(Long groupRoomId);

    UserGroup findUserByGroupId(Long userId, Long groupRoomId);

    boolean existsByUserCodeAndGroupId(String userCode, Long groupId);

    List<UserGroup> findAllUsersByGroupId(Long groupRoomId);
}
