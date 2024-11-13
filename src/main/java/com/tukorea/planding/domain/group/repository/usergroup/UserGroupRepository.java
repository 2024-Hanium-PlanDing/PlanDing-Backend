package com.tukorea.planding.domain.group.repository.usergroup;

import com.tukorea.planding.domain.group.entity.UserGroup;
import com.tukorea.planding.domain.group.entity.domain.UserGroupDomain;
import com.tukorea.planding.domain.user.entity.UserDomain;

import java.util.List;

public interface UserGroupRepository {
    boolean existsByGroupRoomAndUser(String groupCode, String userCode);

    List<UserDomain> findUserByIsConnectionFalse(Long groupRoomId);

    UserGroupDomain findUserByGroupId(Long userId, Long groupRoomId);

    UserGroupDomain save(UserGroupDomain userGroupDomain);
    void delete(UserGroupDomain userGroupDomain);
}
