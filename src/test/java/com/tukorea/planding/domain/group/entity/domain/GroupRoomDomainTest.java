package com.tukorea.planding.domain.group.entity.domain;

import com.tukorea.planding.domain.group.dto.request.GroupCreateRequest;
import com.tukorea.planding.domain.group.dto.request.GroupUpdateRequest;
import com.tukorea.planding.domain.user.dto.UserResponse;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GroupRoomDomainTest {

    private UserDomain owner;
    private GroupRoomDomain groupRoom;

    @BeforeEach
    void setUp() {
        owner = UserDomain.builder()
                .userCode("#1234")
                .username("ownerName")
                .build();

        groupRoom = GroupRoomDomain.builder()
                .id(1L)
                .name("Test")
                .description("Test Group Hello World")
                .owner(owner)
                .groupCode("G12345")
                .thumbnail("default.png")
                .alarm(true)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    @Test
    void GroupRoom_을_생성_할_수_있다() {
        //given
        GroupCreateRequest request = GroupCreateRequest.builder()
                .name("New")
                .description("HelloWorld")
                .build();

        //when
        GroupRoomDomain result = GroupRoomDomain.createGroupRoom(request, User.fromModel(owner));

        //then
        assertThat(result.getName()).isEqualTo("New");
        assertThat(result.getDescription()).isEqualTo("HelloWorld");
        assertThat(result.getOwner().getUserCode()).isEqualTo("#1234");
    }

    @Test
    void GroupRoom_소유자_검증_테스트_성공() {
        UserResponse userResponse = UserResponse.builder()
                .userCode(owner.getUserCode())
                .username(owner.getUsername())
                .build();

        groupRoom.validateOwner(userResponse);
    }

    @Test
    void GroupRoom_소유자_검증_테스트_실패() {
        UserResponse userResponse = UserResponse.builder()
                .userCode("#7890")
                .username("other")
                .build();

        assertThatThrownBy(() -> groupRoom.validateOwner(userResponse))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.ACCESS_DENIED.getMessage());
    }

    @Test
    void GroupRoom_그룹의_이름_및_설명_업데이트() {
        GroupUpdateRequest request = GroupUpdateRequest.builder()
                .groupId(1L)
                .name("Update Name")
                .description("Update Description")
                .build();

        GroupRoomDomain result = groupRoom.update(request);

        assertThat(result.getName()).isEqualTo("Update Name");
        assertThat(result.getDescription()).isEqualTo("Update Description");
        assertThat(result.getModifiedDate()).isAfterOrEqualTo(groupRoom.getModifiedDate());
    }

    @Test
    void GroupRoom_그룹의_알람_업데이트(){
        GroupRoomDomain result = groupRoom.updateAlarm(false);

        assertThat(result.isAlarm()).isFalse();
        assertThat(result.getModifiedDate()).isAfterOrEqualTo(groupRoom.getModifiedDate());
    }

    @Test
    void GroupRoom_그룹의_썸네일_업데이트(){
        GroupRoomDomain updatedGroupRoom = groupRoom.updateThumbnail("newThumbnail.png");

        assertThat(updatedGroupRoom.getThumbnail()).isEqualTo("newThumbnail.png");
        assertThat(updatedGroupRoom.getModifiedDate()).isAfterOrEqualTo(groupRoom.getModifiedDate());
    }
}
