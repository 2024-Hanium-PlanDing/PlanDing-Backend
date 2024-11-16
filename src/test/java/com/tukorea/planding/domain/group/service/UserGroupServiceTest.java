package com.tukorea.planding.domain.group.service;

import com.tukorea.planding.common.service.GroupRoomCodeHolder;
import com.tukorea.planding.common.service.UserCodeHolder;
import com.tukorea.planding.domain.group.entity.domain.GroupRoomDomain;
import com.tukorea.planding.domain.group.entity.domain.UserGroupDomain;
import com.tukorea.planding.domain.group.repository.normal.GroupRoomRepository;
import com.tukorea.planding.domain.group.repository.usergroup.UserGroupRepository;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.domain.user.repository.UserRepository;
import com.tukorea.planding.domain.user.service.UserQueryService;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import com.tukorea.planding.global.oauth.details.Role;
import com.tukorea.planding.mock.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class UserGroupServiceTest {

    private UserDomain userDomain;
    private GroupRoomDomain groupRoomDomain;
    private String userCode;
    private String groupCode;
    private UserGroupService userGroupService;
    private UserGroupRepository userGroupRepository;

    @BeforeEach
    void init() {

        UserRepository userRepository = new FakeUserRepository();
        GroupRoomRepository groupRoomRepository = new FakeGroupRoomRepository();
        userGroupRepository = new FakeUserGroupRepository();

        UserQueryService userQueryService = new UserQueryService(userRepository);

        this.userGroupService = UserGroupService.builder()
                .userGroupRepository(userGroupRepository)
                .groupRoomRepository(groupRoomRepository)
                .userQueryService(userQueryService)
                .build();


        UserCodeHolder testUserCodeHolder = new TestUserCodeHolder("#1234");
        userCode = testUserCodeHolder.userCode();
        userDomain = UserDomain.builder()
                .id(1L)
                .email("ksu9541@tukorea.ac.kr")
                .username("ksu9541")
                .userCode(userCode)
                .fcmToken("aaaa-aaaa-aaaa-aaaa")
                .alarm(true)
                .role(Role.USER)
                .build();

        GroupRoomCodeHolder testGroupCodeHolder = new TestGroupCodeHolder("G1234");
        groupCode = testGroupCodeHolder.groupCode();
        groupRoomDomain = GroupRoomDomain.builder()
                .id(1L)
                .owner(userDomain)
                .thumbnail("thumbnail.png")
                .description("description")
                .groupCode(groupCode)
                .name("name")
                .alarm(true)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .groupFavorites(new ArrayList<>())
                .build();

        UserGroupDomain userGroupDomain = UserGroupDomain.builder()
                .isConnected(false)
                .user(userDomain)
                .groupRoom(groupRoomDomain)
                .build();


        groupRoomDomain = groupRoomDomain.addUserGroup(userGroupDomain);

        userDomain = userDomain.addUserGroup(userGroupDomain);
        userDomain = userRepository.save(userDomain);
        userGroupRepository.save(userGroupDomain);
        groupRoomRepository.save(groupRoomDomain);

    }

    @Test
    void 그룹코드와_유저코드가_일치하면_접속여부를_True로_변경할_수_있다() {
        userGroupService.updateConnectionStatus(userCode, groupCode, true);

        UserGroupDomain result = userGroupRepository.findUserByGroupId(userDomain.getId(), groupRoomDomain.getId());

        assertThat(result.isConnected()).isTrue();
    }

    @Test
    void 그룹코드와_유저코드가_일치하면_접속여부를_False로_변경할_수_있다() {
        userGroupService.updateConnectionStatus(userCode, groupCode, false);

        UserGroupDomain result = userGroupRepository.findUserByGroupId(userDomain.getId(), groupRoomDomain.getId());

        assertThat(result.isConnected()).isFalse();
    }

    @Test
    void 유효하지_않은_그룹코드가_입력되면_BusinessException이_발생한다() {
        assertThatThrownBy(() -> userGroupService.updateConnectionStatus(userCode, "other", false))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.GROUP_ROOM_NOT_FOUND.getMessage());
    }


}