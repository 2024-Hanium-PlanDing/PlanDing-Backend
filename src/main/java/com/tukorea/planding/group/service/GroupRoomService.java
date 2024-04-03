package com.tukorea.planding.group.service;

import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import com.tukorea.planding.group.dao.GroupRoomRepository;
import com.tukorea.planding.group.dao.UserGroupMembershipRepository;
import com.tukorea.planding.group.domain.GroupRoom;
import com.tukorea.planding.group.dto.RequestCreateGroupRoom;
import com.tukorea.planding.group.dto.RequestInviteGroupRoom;
import com.tukorea.planding.group.dto.ResponseGroupRoom;
import com.tukorea.planding.user.dao.UserRepository;
import com.tukorea.planding.user.domain.User;
import com.tukorea.planding.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupRoomService {

    private final UserRepository userRepository;
    private final UserGroupMembershipRepository userGroupMembershipRepository;
    private final GroupRoomRepository groupRoomRepository;

    @Transactional
    public ResponseGroupRoom createGroupRoom(UserInfo userInfo, RequestCreateGroupRoom createGroupRoom) {
        User user = userRepository.findByEmail(userInfo.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        GroupRoom newGroupRoom = GroupRoom.builder()
                .title(createGroupRoom.getTitle())
                .owner(user.getCode())
                .build();

        newGroupRoom.addUser(user);
        GroupRoom savedGroupRoom = groupRoomRepository.save(newGroupRoom);

        // 중간테이블에 유저, 그룹 정보 저장
        userGroupMembershipRepository.saveAll(newGroupRoom.getGroupMemberships());

        return ResponseGroupRoom.from(savedGroupRoom);
    }

    @Transactional
    public ResponseGroupRoom inviteGroupRoom(UserInfo userInfo, RequestInviteGroupRoom invitedUserInfo) {
        RequestInviteGroupRoom checking = RequestInviteGroupRoom.checking(invitedUserInfo);

        // 초대하는 유저가 존재하는지 체크하는 로직
        User invitingUser = userRepository.findByEmail(userInfo.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        GroupRoom groupRoom = groupRoomRepository.findByGroupCode(invitedUserInfo.getInviteGroupCode())
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_ROOM_NOT_FOUND));

        // 초대하는 유저가 방장인지 체크하는 로직
        validInvitePermission(groupRoom, invitingUser);

        User invitedUser = findUserByRequest(checking);
        groupRoom.addUser(invitedUser);

        // 중간테이블에 유저, 그룹 정보 저장
        userGroupMembershipRepository.saveAll(groupRoom.getGroupMemberships());

        return ResponseGroupRoom.from(groupRoom);
    }

    // 유저가 속한 그룹룸 가져오기
    public List<ResponseGroupRoom> getAllGroupRoomByUser(UserInfo userInfo) {
        User user = userRepository.findByEmail(userInfo.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        List<GroupRoom> groupRooms = groupRoomRepository.findGroupRoomsByUserId(user.getId());

        return groupRooms.stream()
                .map(ResponseGroupRoom::from)
                .collect(Collectors.toList());
    }

    private User findUserByRequest(RequestInviteGroupRoom checking) {
        if (checking.getUserCode() == null) {
            return userRepository.findByEmail(checking.getUserEmail())
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        } else {
            return userRepository.findByCode(checking.getUserCode())
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        }
    }

    private void validInvitePermission(GroupRoom groupRoom, User invitingUser) {
        if (!groupRoom.getOwner().equals(invitingUser.getCode())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_GROUP_ROOM_INVITATION);
        }
    }
}
