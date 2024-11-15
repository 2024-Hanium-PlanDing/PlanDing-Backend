package com.tukorea.planding.mock;

import com.tukorea.planding.domain.group.entity.UserGroup;
import com.tukorea.planding.domain.group.entity.domain.GroupRoomDomain;
import com.tukorea.planding.domain.group.entity.domain.UserGroupDomain;
import com.tukorea.planding.domain.group.repository.usergroup.UserGroupRepository;
import com.tukorea.planding.domain.user.entity.UserDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FakeUserGroupRepository implements UserGroupRepository {

    long count = 0;
    List<UserGroupDomain> userGroupData = new ArrayList<>();

    @Override
    public boolean existsByGroupRoomAndUser(String groupCode, String userCode) {
        return userGroupData.stream()
                .anyMatch(item -> item.getGroupRoom().getGroupCode().equals(groupCode) && item.getUser().getUserCode().equals(userCode));
    }

    @Override
    public List<UserDomain> findUserByIsConnectionFalse(Long groupRoomId) {
        return null;
    }

    @Override
    public UserGroupDomain findUserByGroupId(Long userId, Long groupRoomId) {
        return userGroupData.stream()
                .filter(item -> item.getUser().getId().equals(userId) && item.getGroupRoom().getId().equals(groupRoomId))
                .findFirst().get();
    }

    @Override
    public UserGroupDomain save(UserGroupDomain userGroupDomain) {
        if (userGroupDomain.getId() == null || userGroupDomain.getId() == 0) {
            UserGroupDomain newData = UserGroupDomain.builder()
                    .id(count++)
                    .groupRoom(userGroupDomain.getGroupRoom())
                    .user(userGroupDomain.getUser())
                    .build();
            userGroupData.add(newData);
            return newData;
        } else {
            userGroupData.removeIf(item -> Objects.equals(item.getId(), userGroupDomain.getId()));
            userGroupData.add(userGroupDomain);
            return userGroupDomain;
        }
    }

    @Override
    public void delete(UserGroupDomain userGroupDomain) {
        userGroupData.removeIf(item -> item.getUser().getUserGroup().equals(userGroupDomain));
    }
}
