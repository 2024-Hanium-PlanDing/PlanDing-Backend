package com.tukorea.planding.domain.group.service;

import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.entity.UserGroup;
import com.tukorea.planding.domain.group.repository.normal.GroupRoomRepository;
import com.tukorea.planding.domain.group.repository.usergroup.UserGroupRepository;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.domain.user.service.UserQueryService;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final UserQueryService userQueryService;
    private final GroupRoomRepository groupRoomRepository;


    public void updateConnectionStatus(String userCode, String groupCode, boolean isConnected) {

        UserDomain user = userQueryService.getUserByUserCode(userCode);

        GroupRoom groupRoom = groupRoomRepository.findByGroupCode(groupCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_ROOM_NOT_FOUND));

        if (!userGroupRepository.existsByGroupRoomAndUser(groupCode, user.getUserCode())) {
            throw new BusinessException(ErrorCode.GROUP_ROOM_NOT_FOUND);
        }

        UserGroup test = userGroupRepository.findUserByGroupId(user.getId(), groupRoom.getId());
        test.setConnected(isConnected);
    }

}
