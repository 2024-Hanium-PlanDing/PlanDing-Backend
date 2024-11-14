package com.tukorea.planding.domain.group.service;

import com.tukorea.planding.common.service.GroupRoomCodeHolder;
import com.tukorea.planding.domain.chat.service.ChatRoomService;
import com.tukorea.planding.domain.group.dto.request.GroupCreateRequest;
import com.tukorea.planding.domain.group.dto.request.GroupUpdateRequest;
import com.tukorea.planding.domain.group.dto.response.GroupInformationResponse;
import com.tukorea.planding.domain.group.dto.response.GroupResponse;
import com.tukorea.planding.domain.group.entity.domain.GroupRoomDomain;
import com.tukorea.planding.domain.group.entity.domain.UserGroupDomain;
import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.group.service.query.UserGroupQueryService;
import com.tukorea.planding.domain.user.dto.UserResponse;
import com.tukorea.planding.domain.user.dto.UserInfoSimple;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.domain.user.service.UserQueryService;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Builder
@Transactional
@RequiredArgsConstructor
public class GroupRoomService {

    private final UserQueryService userQueryService;
    private final UserGroupQueryService userGroupQueryService;
    private final GroupQueryService groupQueryService;
    private final GroupRoomFactory groupRoomFactory;
    private final ChatRoomService chatRoomService;
    private final GroupRoomCodeHolder groupRoomCodeHolder;


    public GroupResponse createGroupRoom(UserResponse userResponse, GroupCreateRequest createGroupRoom, MultipartFile thumbnailFile) {
        UserDomain user = userQueryService.getUserByUserCode(userResponse.getUserCode());
        String groupCode = groupRoomCodeHolder.groupCode(); // 그룹코드 생성

        GroupRoomDomain newGroupRoom = groupRoomFactory.createGroupRoom(createGroupRoom, groupCode, user, thumbnailFile);
        GroupRoomDomain savedGroupRoom = groupQueryService.createGroup(newGroupRoom);
        UserGroupDomain userGroup = UserGroupDomain.createUserGroup(user, savedGroupRoom);

        // 중간테이블에 유저, 그룹 정보 저장
        userGroupQueryService.save(userGroup);
        chatRoomService.createChatRoomAfterCommit(groupCode);
        return GroupResponse.toGroupResponse(newGroupRoom);
    }

    public GroupResponse updateGroupNameOrDescription(UserResponse userResponse, GroupUpdateRequest groupUpdateRequest) {
        GroupRoomDomain groupRoomDomain = groupQueryService.getGroupById(groupUpdateRequest.groupId());
        groupRoomDomain.validateOwner(userResponse);     // TODO 그룹의 팀원도 변경가능하도록
        groupRoomDomain = groupRoomDomain.update(groupUpdateRequest);
        return GroupResponse.toGroupResponse(groupRoomDomain);
    }

    public void deleteGroup(UserResponse userResponse, String groupCode) {
        GroupRoomDomain groupRoomDomain = groupQueryService.getGroupByCode(groupCode);
        groupRoomDomain.validateOwner(userResponse);
        groupQueryService.delete(groupRoomDomain);
    }

    // 유저가 속한 그룹룸 가져오기
    public List<GroupResponse> getAllGroupRoomByUser(UserResponse userResponse, PageRequest request) {
        List<GroupRoomDomain> groupRooms = groupQueryService.findGroupsByUserId(userResponse.getId(), request);
        return groupRooms.stream()
                .map(GroupResponse::toGroupResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GroupInformationResponse getGroupUsers(UserResponse userResponse, String groupCode) {
        GroupRoomDomain groupRoom = groupQueryService.getGroupByCode(groupCode);
        groupRoom.hasMember(userResponse); // 검사
        List<UserInfoSimple> userInfoSimples = userQueryService.findByUserGroupGroupCode(groupCode)
                .stream()
                .map(user -> UserInfoSimple.fromEntity(user, groupRoom.getOwner().getUserCode()))
                .collect(Collectors.toList());
        return GroupInformationResponse.from(groupRoom, userInfoSimples, userResponse);
    }

    public void leaveGroup(UserResponse userResponse, String groupCode) {
        GroupRoomDomain groupRoom = groupQueryService.getGroupByCode(groupCode);
        UserGroupDomain userGroup = userGroupQueryService.findByUserIdAndGroupId(userResponse.getId(), groupRoom.getId());
        if (groupRoom.isGroupOwner(userResponse)) {
            groupQueryService.delete(groupRoom);
        } else {
            userGroupQueryService.delete(userGroup);
        }
        chatRoomService.deleteChatRoom(groupCode);
    }

    public void updateGroupRoomAlarmSetting(String userCode, String groupCode, boolean alarmEnabled) {
        if (!groupQueryService.existGroupInUser(groupCode, userCode)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        GroupRoomDomain groupRoom = groupQueryService.getGroupByCode(groupCode);
        groupRoom.updateAlarm(alarmEnabled);
    }
}
