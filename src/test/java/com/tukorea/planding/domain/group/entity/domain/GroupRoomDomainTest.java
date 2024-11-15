package com.tukorea.planding.domain.group.entity.domain;

import com.tukorea.planding.domain.group.dto.request.GroupCreateRequest;
import com.tukorea.planding.domain.group.dto.request.GroupUpdateRequest;
import com.tukorea.planding.domain.user.dto.UserResponse;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class GroupRoomDomainTest {

    private UserDomain owner;
    private GroupRoomDomain groupRoomDomain;

    @BeforeEach
    void init() {
        owner = UserDomain.builder()
                .username("name")
                .userCode("#1234")
                .build();

        groupRoomDomain = GroupRoomDomain.builder()
                .id(1L)
                .name("Test")
                .description("HelloWorld")
                .owner(owner)
                .thumbnail("test.png")
                .alarm(true)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    @Test
    void 그룹을_생성_할_수_있다() {
        GroupCreateRequest request = GroupCreateRequest.builder()
                .name("Hello")
                .description("World")
                .build();

        UserDomain userDomain = UserDomain.builder()
                .userCode("#0000")
                .username("owner")
                .build();

        GroupRoomDomain newGroupRoom = GroupRoomDomain.createGroupRoom(request, "test", userDomain);

        assertThat(newGroupRoom.getName()).isEqualTo("Hello");
        assertThat(newGroupRoom.getDescription()).isEqualTo("World");
        assertThat(newGroupRoom.getGroupCode()).isEqualTo("test");
        assertThat(newGroupRoom.getOwner().getUserCode()).isEqualTo("#0000");
    }

    @Test
    void 그룹의_소유자_를_검증_할_수_있다_성공() {
        UserResponse userResponse = UserResponse.toResponse(owner);

        groupRoomDomain.validateOwner(userResponse);
    }

    @Test
    void 그룹의_소유자_를_검증_할_수_있다_실패() {
        UserDomain other = UserDomain.builder()
                .userCode("#0000")
                .build();

        assertThatThrownBy(() -> groupRoomDomain.validateOwner(UserResponse.toResponse(other)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.ACCESS_DENIED.getMessage());
    }

    @Test
    void 그룹_이름_및_설명_을_업데이트_할_수_있다() {
        // given
        GroupUpdateRequest updateRequest = GroupUpdateRequest.builder()
                .name("other")
                .description("other")
                .build();

        // when
        GroupRoomDomain updatedGroupRoom = groupRoomDomain.update(updateRequest);

        // then
        assertThat(updatedGroupRoom.getName()).isEqualTo("other");
        assertThat(updatedGroupRoom.getDescription()).isEqualTo("other");
        assertThat(updatedGroupRoom.getModifiedDate()).isAfterOrEqualTo(groupRoomDomain.getModifiedDate());
    }

    @Test
    void 그룹의_알람_업데이트를_진행_할_수_있다() {
        //when
        GroupRoomDomain updatedGroupRoom = groupRoomDomain.updateAlarm(false);

        // then
        assertThat(updatedGroupRoom.isAlarm()).isFalse();
        assertThat(updatedGroupRoom.getModifiedDate()).isAfterOrEqualTo(groupRoomDomain.getModifiedDate());
    }

    @Test
    void 그룹의_썸네일을_업데이트_할_수_있다() {
        // when
        GroupRoomDomain updatedGroupRoom = groupRoomDomain.updateThumbnail("newThumbnail.png");

        // then
        assertThat(updatedGroupRoom.getThumbnail()).isEqualTo("newThumbnail.png");
        assertThat(updatedGroupRoom.getModifiedDate()).isAfterOrEqualTo(groupRoomDomain.getModifiedDate());
    }

    @Test
    void 그룹의_오너인지_확인_한다() {
        UserResponse userResponse = UserResponse.toResponse(owner);
        assertThat(groupRoomDomain.isGroupOwner(userResponse)).isTrue();
    }

    @Test
    void 그룹에서_유저를_추가할_수_있다(){
        Set<UserGroupDomain> userGroupDomains=new HashSet<>();
        userGroupDomains.add(UserGroupDomain.builder().build());
        
        GroupRoomDomain result = groupRoomDomain.addUserGroup(userGroupDomains);

        assertThat(result.getUserGroups().size()).isEqualTo(1);
    }
}