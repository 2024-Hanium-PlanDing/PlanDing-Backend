package com.tukorea.planding.domain.group.service;

import com.tukorea.planding.domain.group.dto.request.GroupInviteRequest;
import com.tukorea.planding.domain.group.dto.response.GroupInviteMessageResponse;
import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.repository.normal.GroupRoomRepository;
import com.tukorea.planding.domain.group.service.GroupInviteService;
import com.tukorea.planding.domain.group.service.GroupRoomService;
import com.tukorea.planding.domain.group.service.UserGroupService;
import com.tukorea.planding.domain.group.service.query.UserGroupQueryService;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.mapper.UserMapper;
import com.tukorea.planding.domain.user.repository.UserRepository;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.oauth.details.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GroupInviteServiceTestINT {

    @Autowired
    private UserGroupService userGroupService;
    @Autowired
    private GroupInviteService groupInviteService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRoomService groupRoomService;
    @Autowired
    private GroupRoomRepository groupRoomRepository;
    @Autowired
    private UserGroupQueryService userGroupQueryService;

    private User userA;
    private User userB;
    private User userC;

    @BeforeEach
    public void setup() {
        userA = createUser("userA", "#abcd");
        userB = createUser("userB", "#1234");
        userC = createUser("userC", "#qwer");
    }

    private User createUser(String email, String userCode) {
        User user = User.builder()
                .email(email)
                .username(email)
                .role(Role.USER)
                .userCode(userCode)
                .build();
        return userRepository.save(user);
    }

    private GroupRoom createGroupRoom(User owner, String name, String description) {
        return groupRoomRepository.save(GroupRoom.builder()
                .owner(owner.getUserCode())
                .groupCode("#group")
                .name(name)
                .description(description)
                .build());
    }

    @Test
    @DisplayName("유저 A가 그룹방을 생성한다.")
    void createGroupRoom() {
        GroupRoom groupRoom = createGroupRoom(userA, "first_group", "description");

        Optional<GroupRoom> getGroup = groupRoomRepository.findById(groupRoom.getId());

        assertTrue(getGroup.isPresent());
        assertEquals(groupRoom.getGroupCode(), getGroup.get().getGroupCode());
        assertEquals(groupRoom.getOwner(), getGroup.get().getOwner());
        assertEquals(groupRoom.getDescription(), getGroup.get().getDescription());
    }

    @Test
    @DisplayName("서비스 코드 inviteGroupRoom 테스트")
    public void inviteGroupService() {
        GroupRoom groupRoom = createGroupRoom(userA, "group_name", "description");

        GroupInviteRequest groupInviteRequest = GroupInviteRequest.builder()
                .groupCode(groupRoom.getGroupCode())
                .userCode(userB.getUserCode())
                .build();

        GroupInviteMessageResponse groupInviteMessageResponse = groupInviteService.inviteGroupRoom(UserMapper.toUserInfo(userA), groupInviteRequest);
        groupInviteService.acceptInvitation(UserMapper.toUserInfo(userB), groupInviteMessageResponse.getInviteCode(), groupRoom.getGroupCode());

        assertEquals(1, groupInviteService.getInvitations(UserMapper.toUserInfo(userB)).size());
    }

    @Test
    @DisplayName("서비스 코드 inviteGroupRoom 실패 테스트")
    public void failInviteGroupService() {
        GroupRoom groupRoom = createGroupRoom(userA, "group_name", "description");

        GroupInviteRequest groupInviteRequest = GroupInviteRequest.builder()
                .groupId(groupRoom.getId())
                .userCode(userB.getUserCode())
                .build();

        assertThrows(BusinessException.class, () ->
                groupInviteService.inviteGroupRoom(UserMapper.toUserInfo(userC), groupInviteRequest));
    }
}