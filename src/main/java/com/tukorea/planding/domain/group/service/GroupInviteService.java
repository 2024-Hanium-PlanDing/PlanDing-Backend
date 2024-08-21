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
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.entity.User;
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
    private final RedisGroupInviteService redisGroupInviteService;
    private final ChatRoomRepository chatRoomRepository;


    public GroupInviteMessageResponse inviteGroupRoom(UserInfo userInfo, GroupInviteRequest groupInviteRequest) {
        // 초대하는 사용자와 초대 대상 사용자가 같은지 확인
        if (userInfo.getUserCode().equals(groupInviteRequest.userCode())) {
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
        if (!groupQueryService.existGroupInUser(groupInviteRequest.groupCode(), userInfo.getUserCode())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        GroupRoom group = groupQueryService.getGroupByCode(groupInviteRequest.groupCode());
        
        GroupInviteMessageResponse groupInviteMessageResponse = GroupInviteMessageResponse.create("IN" + UUID.randomUUID(), group.getGroupCode(), group.getName(), groupInviteRequest.userCode(), userInfo.getUsername(), GroupResponse.from(group), LocalDateTime.now());

        redisGroupInviteService.createInvitation(groupInviteRequest.userCode(), groupInviteMessageResponse);

        eventHandler.notifyInvitation(groupInviteRequest.userCode(), group.getName());

        return groupInviteMessageResponse;
    }

    public GroupInviteAcceptResponse acceptInvitation(UserInfo userInfo, String code, String groupCode) {
        User user = userQueryService.getUserByUserCode(userInfo.getUserCode());
        GroupRoom group = groupQueryService.getGroupByCode(groupCode);

        final UserGroup userGroup = UserGroup.createUserGroup(user, group);
        userGroupQueryService.save(userGroup);

        redisGroupInviteService.deleteInvitation(userInfo.getUserCode(), code);

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
    public List<GroupInviteMessageResponse> getInvitations(UserInfo userInfo) {
        return redisGroupInviteService.getAllInvitations(userInfo.getUserCode());
    }

    public void declineInvitation(UserInfo userInfo, String inviteCode) {
        redisGroupInviteService.deleteInvitation(userInfo.getUserCode(), inviteCode);
    }
}
