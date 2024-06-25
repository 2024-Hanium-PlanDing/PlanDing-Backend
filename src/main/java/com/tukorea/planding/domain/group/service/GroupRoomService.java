package com.tukorea.planding.domain.group.service;

import com.tukorea.planding.domain.group.dto.request.GroupCreateRequest;
import com.tukorea.planding.domain.group.dto.request.GroupUpdateRequest;
import com.tukorea.planding.domain.group.dto.response.GroupResponse;
import com.tukorea.planding.domain.group.dto.response.GroupUserResponse;
import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.entity.UserGroup;
import com.tukorea.planding.domain.group.repository.normal.GroupRoomRepository;
import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.group.service.query.UserGroupQueryService;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.service.UserQueryService;
import com.tukorea.planding.global.config.s3.S3Uploader;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
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

    @Transactional
    public GroupResponse createGroupRoom(UserInfo userInfo, GroupCreateRequest createGroupRoom, MultipartFile thumbnailFile) {
        User user = userQueryService.getUserByUserCode(userInfo.getUserCode());

        GroupRoom newGroupRoom = groupRoomFactory.createGroupRoom(createGroupRoom, user, thumbnailFile);
        GroupRoom savedGroupRoom = groupQueryService.createGroup(newGroupRoom);
        final UserGroup userGroup = UserGroup.createUserGroup(user, savedGroupRoom);

        // 중간테이블에 유저, 그룹 정보 저장
        userGroupQueryService.save(userGroup);

        return toGroupResponse(newGroupRoom);
    }

    @Transactional
    public GroupResponse updateGroupNameOrDescription(UserInfo userInfo, GroupUpdateRequest groupUpdateRequest) {
        GroupRoom groupRoom = groupQueryService.getGroupById(groupUpdateRequest.groupId());

        // TODO 그룹의 팀원도 변경가능하도록
        if (!groupRoom.getOwner().equals(userInfo.getUserCode())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        groupRoom.updateName(groupUpdateRequest.name());
        groupRoom.updateDescription(groupUpdateRequest.description());

        return toGroupResponse(groupRoom);
    }

    public void deleteGroup(UserInfo userInfo, Long groupId) {
        User user = userQueryService.getUserByUserCode(userInfo.getUserCode());
        GroupRoom groupRoom = groupQueryService.getGroupById(groupId);

        if (!groupRoom.getOwner().equals(user.getUserCode())) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        groupQueryService.delete(groupRoom);
    }

    // 유저가 속한 그룹룸 가져오기
    public List<GroupResponse> getAllGroupRoomByUser(UserInfo userInfo) {
        List<GroupRoom> groupRooms = groupQueryService.findGroupsByUserId(userInfo.getId());
        return groupRooms.stream()
                .sorted(Comparator.comparing(GroupRoom::getCreatedDate).reversed())
                .map(this::toGroupResponse)
                .collect(Collectors.toList());
    }

    public List<GroupUserResponse> getGroupUsers(Long groupId) {
        List<User> users = groupQueryService.getGroupUsers(groupId);
        return users.stream()
                .map(GroupUserResponse::toGroupUserResponse)
                .collect(Collectors.toList());
    }

    public void leaveGroup(UserInfo userInfo, Long groupId) {
        GroupRoom groupRoom = groupQueryService.getGroupById(groupId);
        UserGroup userGroup = userGroupQueryService.findByUserIdAndGroupId(userInfo.getId(), groupId);

        if (groupRoom.getOwner().equals(userInfo.getUserCode())) {
            groupQueryService.delete(groupRoom);
        } else {
            userGroupQueryService.delete(userGroup);
        }
    }

    private GroupResponse toGroupResponse(GroupRoom groupRoom) {
        return GroupResponse.from(groupRoom);
    }
}
