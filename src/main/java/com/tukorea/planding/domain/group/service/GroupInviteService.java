package com.tukorea.planding.domain.group.service;

import com.tukorea.planding.domain.chat.repository.ChatRoomRepository;
import com.tukorea.planding.domain.group.dto.request.GroupInviteRequest;
import com.tukorea.planding.domain.group.dto.response.GroupInviteAcceptResponse;
import com.tukorea.planding.domain.group.dto.response.GroupInviteMessageResponse;
import com.tukorea.planding.domain.group.dto.response.GroupResponse;
import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.entity.UserGroup;
import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.group.service.query.UserGroupQueryService;
import com.tukorea.planding.domain.notify.service.NotificationEventHandler;
import com.tukorea.planding.domain.user.dto.UserResponse;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.domain.user.service.UserQueryService;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupInviteService {
    private final UserQueryService userQueryService;
    private final GroupQueryService groupQueryService;
    private final UserGroupQueryService userGroupQueryService;
    private final NotificationEventHandler eventHandler;
    private final RedisGroupInviteServiceImpl redisGroupInviteServiceImpl;
    private final ChatRoomRepository chatRoomRepository;


    public GroupInviteMessageResponse inviteGroupRoom(UserResponse userResponse, GroupInviteRequest groupInviteRequest) {
        // 초대하는 사용자와 초대 대상 사용자가 같은지 확인
        if (userResponse.getUserCode().equals(groupInviteRequest.userCode())) {
            throw new BusinessException(ErrorCode.CANNOT_INVITE_YOURSELF);
        }
        // 그룹이 존재하는지
        if (!groupQueryService.existsByGroupCode(groupInviteRequest.groupCode())) {
            throw new BusinessException(ErrorCode.GROUP_ROOM_NOT_FOUND);
        }
        // 그룹안에 초대자가 존재하는지
        if (groupQueryService.existGroupInUser(groupInviteRequest.groupCode(), groupInviteRequest.userCode())) {
            throw new BusinessException(ErrorCode.USER_ALREADY_IN_GROUP);
        }
        // 그룹안에 속해있는지
        if (!groupQueryService.existGroupInUser(groupInviteRequest.groupCode(), userResponse.getUserCode())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        GroupRoom group = groupQueryService.getGroupByCode(groupInviteRequest.groupCode());

        GroupInviteMessageResponse groupInviteMessageResponse = GroupInviteMessageResponse.create("IN" + UUID.randomUUID(), group.getGroupCode(), group.getName(), groupInviteRequest.userCode(), userResponse.getUsername(), GroupResponse.from(group), LocalDateTime.now());

        redisGroupInviteServiceImpl.createInvitation(groupInviteRequest.userCode(), groupInviteMessageResponse);

        eventHandler.notifyInvitation(groupInviteRequest.userCode(), group.getName());

        return groupInviteMessageResponse;
    }

    public GroupInviteAcceptResponse acceptInvitation(UserResponse userResponse, String code, String groupCode) {
        UserDomain user = userQueryService.getUserByUserCode(userResponse.getUserCode());
        GroupRoom group = groupQueryService.getGroupByCode(groupCode);

        final UserGroup userGroup = UserGroup.createUserGroup(User.fromModel(user), group);
        userGroupQueryService.save(userGroup);

        redisGroupInviteServiceImpl.deleteInvitation(userResponse.getUserCode(), code);

        /** 채팅방 참여 **/
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                chatRoomRepository.enterChatRoom(groupCode);

            }
        });

        return GroupInviteAcceptResponse.builder().groupCode(groupCode).build();
    }

    @Transactional(readOnly = true)
    public List<GroupInviteMessageResponse> getInvitations(UserResponse userResponse) {
        return redisGroupInviteServiceImpl.getAllInvitations(userResponse.getUserCode());
    }

    public void declineInvitation(UserResponse userResponse, String inviteCode) {
        redisGroupInviteServiceImpl.deleteInvitation(userResponse.getUserCode(), inviteCode);
    }
}
