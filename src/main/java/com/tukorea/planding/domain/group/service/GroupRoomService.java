package com.tukorea.planding.domain.group.service;

import com.tukorea.planding.domain.chat.repository.ChatRoomRepository;
import com.tukorea.planding.domain.chat.service.ChatRoomService;
import com.tukorea.planding.domain.group.dto.request.GroupCreateRequest;
import com.tukorea.planding.domain.group.dto.request.GroupUpdateRequest;
import com.tukorea.planding.domain.group.dto.response.GroupInformationResponse;
import com.tukorea.planding.domain.group.dto.response.GroupResponse;
import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.entity.UserGroup;
import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.group.service.query.UserGroupQueryService;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.dto.UserInfoSimple;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.service.UserQueryService;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class GroupRoomService {

    private final UserQueryService userQueryService;
    private final UserGroupQueryService userGroupQueryService;
    private final GroupQueryService groupQueryService;
    private final GroupRoomFactory groupRoomFactory;
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;


    public GroupResponse createGroupRoom(UserInfo userInfo, GroupCreateRequest createGroupRoom, MultipartFile thumbnailFile) {
        User user = userQueryService.getUserByUserCode(userInfo.getUserCode());

        GroupRoom newGroupRoom = groupRoomFactory.createGroupRoom(createGroupRoom, user, thumbnailFile);
        GroupRoom savedGroupRoom = groupQueryService.createGroup(newGroupRoom);
        final UserGroup userGroup = UserGroup.createUserGroup(user, savedGroupRoom);

        // 중간테이블에 유저, 그룹 정보 저장
        userGroupQueryService.save(userGroup);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                chatRoomService.createChatRoomForGroup(savedGroupRoom.getGroupCode());
            }
        });
        return toGroupResponse(newGroupRoom);
    }

    public GroupResponse updateGroupNameOrDescription(UserInfo userInfo, GroupUpdateRequest groupUpdateRequest) {
        GroupRoom groupRoom = groupQueryService.getGroupById(groupUpdateRequest.groupId());

        // TODO 그룹의 팀원도 변경가능하도록
        validateOwner(userInfo,groupRoom);

        groupRoom.updateName(groupUpdateRequest.name());
        groupRoom.updateDescription(groupUpdateRequest.description());

        return toGroupResponse(groupRoom);
    }

    public void deleteGroup(UserInfo userInfo, String groupCode) {
        GroupRoom groupRoom = groupQueryService.getGroupByCode(groupCode);

        validateOwner(userInfo,groupRoom);

        groupQueryService.delete(groupRoom);
    }

    // 유저가 속한 그룹룸 가져오기
    public List<GroupResponse> getAllGroupRoomByUser(UserInfo userInfo, PageRequest request) {
        List<GroupRoom> groupRooms = groupQueryService.findGroupsByUserId(userInfo.getId(), request);
        return groupRooms.stream()
                .map(this::toGroupResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GroupInformationResponse getGroupUsers(UserInfo userInfo, String groupCode) {
        GroupRoom groupRoom = groupQueryService.getGroupByCode(groupCode);

        boolean isMember = groupRoom.getUserGroups().stream()
                .anyMatch(userGroup -> userGroup.getUser().getUserCode().equals(userInfo.getUserCode()));

        if (!isMember) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        List<UserInfoSimple> userInfoSimples = groupQueryService.getGroupUsers(groupCode)
                .stream()
                .map(user -> UserInfoSimple.fromEntity(user, groupRoom.getOwner().getUserCode()))
                .collect(Collectors.toList());

        return GroupInformationResponse.builder()
                .id(groupRoom.getId())
                .owner(groupRoom.getOwner().getUserCode())
                .users(userInfoSimples)
                .groupCode(groupRoom.getGroupCode())
                .name(groupRoom.getName())
                .description(groupRoom.getDescription())
                .createdBy(LocalDate.from(groupRoom.getCreatedDate()))
                .thumbnailUrl(groupRoom.getThumbnail())
                .isGroupAdmin(isGroupOwner(userInfo,groupRoom))
                .isFavorite(groupRoom.getGroupFavorites().stream().anyMatch(a -> a.getGroupRoom().getGroupCode().equals(groupCode)))
                .isAlarm(groupRoom.isAlarm())
                .build();
    }

    public void leaveGroup(UserInfo userInfo, String groupCode) {
        GroupRoom groupRoom = groupQueryService.getGroupByCode(groupCode);
        UserGroup userGroup = userGroupQueryService.findByUserIdAndGroupId(userInfo.getId(), groupRoom.getId());

        if (isGroupOwner(userInfo,groupRoom)) {
            groupQueryService.delete(groupRoom);
        } else {
            userGroupQueryService.delete(userGroup);
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                chatRoomRepository.leaveChatRoom(groupCode);
            }
        });
    }

    private GroupResponse toGroupResponse(GroupRoom groupRoom) {
        return GroupResponse.from(groupRoom);
    }

    private void validateOwner(UserInfo userInfo, GroupRoom groupRoom) {
        if (!groupRoom.getOwner().getUserCode().equals(userInfo.getUserCode())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
    }

    private boolean isGroupOwner(UserInfo userInfo, GroupRoom groupRoom) {
        return groupRoom.getOwner().getUserCode().equals(userInfo.getUserCode());
    }

    public void updateGroupRoomAlarmSetting(String userCode, String groupCode, boolean alarmEnabled) {
        if (!groupQueryService.existGroupInUser(groupCode, userCode)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        GroupRoom groupRoom = groupQueryService.getGroupByCode(groupCode);
        groupRoom.updateAlarm(alarmEnabled);
    }
}
