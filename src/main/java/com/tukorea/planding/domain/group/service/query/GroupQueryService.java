package com.tukorea.planding.domain.group.service.query;

import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.repository.normal.GroupRoomRepository;
import com.tukorea.planding.domain.group.repository.normal.GroupRoomRepositoryCustom;
import com.tukorea.planding.domain.group.repository.usergroup.UserGroupRepository;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupQueryService {

    private final GroupRoomRepository groupRoomRepository;
    private final UserGroupRepository userGroupRepository;

    public GroupRoom createGroup(GroupRoom groupRoom) {
        return groupRoomRepository.save(groupRoom);
    }

    public List<GroupRoom> findGroupsByUserId(Long userId) {
        return groupRoomRepository.findGroupRoomsByUserId(userId);
    }

    public GroupRoom getGroupById(Long groupId) {
        return groupRoomRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_ROOM_NOT_FOUND));
    }

    public GroupRoom getGroupByCode(String groupCode) {
        return groupRoomRepository.findByGroupCode(groupCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_ROOM_NOT_FOUND));
    }


    public void delete(GroupRoom groupRoom) {
        groupRoomRepository.delete(groupRoom);
    }

    public boolean existsByGroupCode(String groupCode) {
        return groupRoomRepository.existsByGroupCode(groupCode);
    }

    public List<User> getGroupUsers(String groupCode) {
        return groupRoomRepository.getGroupUsers(groupCode);
    }

    public boolean existGroupInUser(String userCode, Long groupId) {
        return userGroupRepository.existsByUserCodeAndGroupId(userCode, groupId);
    }

    public boolean existGroupInUser(String groupCode, String userCode) {
        return userGroupRepository.existsByGroupRoomAndUser(groupCode, userCode);
    }

}
