package com.tukorea.planding.domain.group.service;

import com.tukorea.planding.common.service.GroupRoomCodeHolder;
import com.tukorea.planding.common.service.UserCodeHolder;
import com.tukorea.planding.domain.group.dto.response.GroupFavoriteResponse;
import com.tukorea.planding.domain.group.dto.response.GroupResponse;
import com.tukorea.planding.domain.group.entity.domain.GroupFavoriteDomain;
import com.tukorea.planding.domain.group.entity.domain.GroupRoomDomain;
import com.tukorea.planding.domain.group.entity.domain.UserGroupDomain;
import com.tukorea.planding.domain.group.repository.favorite.GroupFavoriteRepository;
import com.tukorea.planding.domain.group.repository.normal.GroupRoomRepository;
import com.tukorea.planding.domain.group.repository.usergroup.UserGroupRepository;
import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.schedule.repository.ScheduleRepository;
import com.tukorea.planding.domain.schedule.repository.group.GroupScheduleAttendanceRepository;
import com.tukorea.planding.domain.schedule.repository.port.GroupScheduleRepository;
import com.tukorea.planding.domain.schedule.service.ScheduleQueryService;
import com.tukorea.planding.domain.user.dto.UserResponse;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class GroupFavoriteServiceTest {

    private UserDomain userDomain;
    private GroupFavoriteRepository groupFavoriteRepository;
    private GroupRoomRepository groupRoomRepository;
    private GroupFavoriteService groupFavoriteService;

    @BeforeEach
    void init() {

        groupFavoriteRepository = new FakeFavoriteGroupRepository();

        ScheduleRepository scheduleRepository = new FakeScheduleRepository();
        GroupScheduleAttendanceRepository groupScheduleAttendanceRepository = new FakeGroupAttendanceRepository();
        ScheduleQueryService scheduleQueryService = new ScheduleQueryService(scheduleRepository, groupScheduleAttendanceRepository);


        GroupScheduleRepository groupScheduleRepository = new FakeGroupScheduleRepository();
        groupRoomRepository = new FakeGroupRoomRepository();
        UserGroupRepository userGroupRepository = new FakeUserGroupRepository();
        GroupQueryService groupQueryService = new GroupQueryService(groupRoomRepository, groupScheduleRepository, userGroupRepository, scheduleQueryService);


        UserRepository userRepository = new FakeUserRepository();
        UserQueryService userQueryService = new UserQueryService(userRepository);


        this.groupFavoriteService = GroupFavoriteService.builder()
                .groupFavoriteRepository(groupFavoriteRepository)
                .groupQueryService(groupQueryService)
                .userQueryService(userQueryService)
                .build();

        UserCodeHolder testUserCodeHolder = new TestUserCodeHolder("#1234");
        this.userDomain = UserDomain.builder()
                .id(1L)
                .email("ksu9541@tukorea.ac.kr")
                .username("ksu9541")
                .userCode(testUserCodeHolder.userCode())
                .fcmToken("aaaa-aaaa-aaaa-aaaa")
                .alarm(true)
                .role(Role.USER)
                .build();

        userRepository.save(this.userDomain);

        GroupRoomCodeHolder testGroupCodeHolder = new TestGroupCodeHolder("G1234");
        GroupRoomDomain groupRoomDomain = GroupRoomDomain.builder()
                .id(1L)
                .owner(this.userDomain)
                .thumbnail("thumbnail.png")
                .description("description")
                .groupCode(testGroupCodeHolder.groupCode())
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

        GroupFavoriteDomain groupFavoriteDomain = GroupFavoriteDomain.builder()
                .userDomain(userDomain)
                .groupRoomDomain(groupRoomDomain)
                .build();

        groupRoomDomain = groupRoomDomain.addUserGroup(userGroupDomain);
        groupRoomDomain = groupRoomDomain.addFavorite(groupFavoriteDomain);

        this.userDomain = this.userDomain.addUserGroup(userGroupDomain);
        this.userDomain = userRepository.save(this.userDomain);
        userGroupRepository.save(userGroupDomain);
        groupRoomRepository.save(groupRoomDomain);
        groupFavoriteRepository.save(groupFavoriteDomain);
    }

    @Test
    void 유저의_즐겨찾기_그룹을_찾을_수_있다() {
        UserResponse userResponse = UserResponse.toResponse(userDomain);
        List<GroupResponse> result = groupFavoriteService.findFavoriteGroupsByUser(userResponse);

        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void 그룹을_즐겨찾기_등록을_할_수_있다() {
        //given
        UserResponse userResponse = UserResponse.toResponse(userDomain);
        String groupCode = new TestGroupCodeHolder("G234").groupCode();
        GroupRoomDomain newGroupDomain = GroupRoomDomain.builder()
                .name("test")
                .description("description")
                .groupCode(groupCode)
                .build();

        groupRoomRepository.save(newGroupDomain);

        //when
        GroupFavoriteResponse result = groupFavoriteService.addFavorite(userResponse, groupCode);

        assertThat(result.groupName()).isEqualTo("test");
        assertThat(result.groupCode()).isEqualTo("G234");
    }

    @Test
    void 등록된_그룹을_등록시_오류를_던진다() {
        //given
        String groupCode = new TestGroupCodeHolder("G234").groupCode();
        UserResponse userResponse = UserResponse.toResponse(userDomain);
        GroupRoomDomain newGroupDomain = GroupRoomDomain.builder()
                .name("test")
                .description("description")
                .groupCode(groupCode)
                .build();

        GroupFavoriteDomain groupFavoriteDomain = GroupFavoriteDomain.builder()
                .groupRoomDomain(newGroupDomain)
                .userDomain(userDomain)
                .build();

        groupFavoriteRepository.save(groupFavoriteDomain);

        //when
        assertThatThrownBy(() -> groupFavoriteService.addFavorite(userResponse, groupCode))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.FAVORITE_ALREADY_ADD.getMessage());

    }

    @Test
    void 즐겨찾기_등록을_해제_할_수_있다() {
        UserResponse userResponse = UserResponse.toResponse(userDomain);
        String groupCode = "G1234";
        groupFavoriteService.deleteFavorite(userResponse, groupCode);

        List<GroupFavoriteDomain> result = groupFavoriteRepository.findFavoriteGroupsByUser(userResponse.getId());

        assertThat(result.size()).isEqualTo(0);
    }

}