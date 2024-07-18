package com.tukorea.planding.domain.group.service;

import com.tukorea.planding.domain.group.dto.request.GroupCreateRequest;
import com.tukorea.planding.domain.group.dto.request.GroupInviteRequest;
import com.tukorea.planding.domain.group.dto.response.GroupInviteMessageResponse;
import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.repository.normal.GroupRoomRepository;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.entity.SocialType;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.mapper.UserMapper;
import com.tukorea.planding.domain.user.repository.UserRepository;
import com.tukorea.planding.global.oauth.details.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class GroupInviteServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupRoomRepository groupRoomRepository;
    @Autowired
    GroupInviteService groupInviteService;

    private List<GroupInviteMessageResponse> invite = new ArrayList<>();
    private User invitedUser;

    @BeforeEach
    void 쿼리문_테스트_초대목록조회() {
        User user = User.builder()
                .email("test@")
                .userCode("#test")
                .socialType(SocialType.KAKAO)
                .username("aaa")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        for (int i = 0; i < 10; i++) {
            GroupRoom group = GroupRoom.createGroupRoom(GroupCreateRequest.builder().name("group" + i).build(), user);
            GroupRoom save = groupRoomRepository.save(group);
        }


        User userB = User.builder()
                .email("test2@")
                .userCode("#test2")
                .socialType(SocialType.KAKAO)
                .username("aaa2")
                .role(Role.USER)
                .build();

        invitedUser = userRepository.save(userB);

        for (int i = 0; i < 10; i++) {
            GroupInviteMessageResponse groupInviteMessageResponse = groupInviteService.inviteGroupRoom(UserInfo.builder().userCode("#test").build(), GroupInviteRequest.builder().groupId(i + 1L).userCode("#test2").build());
            invite.add(groupInviteMessageResponse);
        }

    }

    @Test
    void 초대목록조회() {
        User user = userRepository.findByUserCode("#test2").get();
        Assertions.assertEquals(10, groupInviteService.getInvitations(UserMapper.toUserInfo(user)).size());
    }

    @AfterEach
    void deleteAll() {
        for (GroupInviteMessageResponse a : invite) {
            groupInviteService.declineInvitation(UserMapper.toUserInfo(invitedUser), a.getInviteCode());
        }
    }


}