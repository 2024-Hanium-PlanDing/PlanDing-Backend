package com.tukorea.planding.domain.group.service.query;

import com.tukorea.planding.domain.group.entity.UserGroup;
import com.tukorea.planding.domain.group.entity.domain.UserGroupDomain;
import com.tukorea.planding.domain.group.repository.usergroup.UserGroupRepository;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserGroupQueryService {

    private final UserGroupRepository userGroupRepository;

    public void save(UserGroupDomain userGroupDomain) {
        userGroupRepository.save(userGroupDomain);
    }

    public boolean checkUserAccessToGroupRoom(String groupCode, String userCode) {
        return userGroupRepository.existsByGroupRoomAndUser(groupCode, userCode);
    }

    public List<UserDomain> findUserByIsConnectionFalse(Long groupRoomId) {
        return userGroupRepository.findUserByIsConnectionFalse(groupRoomId);
    }

    public UserGroupDomain findByUserIdAndGroupId(Long userId, Long groupRoomId) {
        return userGroupRepository.findUserByGroupId(userId, groupRoomId);
    }

    public void delete(UserGroupDomain userGroupDomain) {
        userGroupRepository.delete(userGroupDomain);
    }
}
