package com.tukorea.planding.group.service;

import com.tukorea.planding.domain.group.dto.request.GroupInviteRequest;
import com.tukorea.planding.domain.group.dto.response.GroupInviteMessageResponse;
import com.tukorea.planding.domain.group.dto.response.GroupResponse;
import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.service.GroupInviteService;
import com.tukorea.planding.domain.group.service.GroupRoomService;
import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.notify.service.sse.SseEmitterService;
import com.tukorea.planding.domain.user.entity.SocialType;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.mapper.UserMapper;
import com.tukorea.planding.domain.user.service.UserQueryService;
import com.tukorea.planding.global.oauth.details.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Transactional
public class GroupRoomServiceTest2 {

    @InjectMocks
    private GroupRoomService groupRoomService;

    @Mock
    private UserQueryService userQueryService;
    @Mock
    private GroupQueryService groupQueryService;

    @InjectMocks
    private GroupInviteService groupInviteService;


    @Mock
    private SseEmitterService sseEmitterService;


    private User testUserA;
    private User testUserB;
    private GroupRoom groupRoom;
    private GroupInviteRequest groupInviteRequest;

    @BeforeEach
    void setUp() {
        testUserA = new User("test", "profile", "username", Role.USER, SocialType.KAKAO, null, "#testA"); // 테스트용 사용자 정보 초기화
        testUserB = new User("test", "profile", "username", Role.USER, SocialType.KAKAO, null, "#testB"); // 테스트용 사용자 정보 초기화
        groupInviteRequest = new GroupInviteRequest(1L, testUserB.getUserCode());
        groupRoom = new GroupRoom("name", null, testUserA.getUserCode(), "Gcode");
    }

    @Test
    public void 유저가_속한_그룹_가져오기() {
        //given
        List<GroupRoom> dummy = Arrays.asList(GroupRoom.builder().groupCode("Gcode").owner(testUserA.getUserCode()).build());
        when(userQueryService.getUserByUserCode(any())).thenReturn(testUserA);
        when(groupQueryService.findGroupsByUserId(any())).thenReturn(dummy);
        //when
        List<GroupResponse> result = groupRoomService.getAllGroupRoomByUser(UserMapper.toUserInfo(testUserA));
        //then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void 다른_유저를_그룹에_초대한다() {
        when(userQueryService.getUserByUserCode(testUserA.getUserCode())).thenReturn(testUserA);
        when(userQueryService.getUserByUserCode(groupInviteRequest.userCode())).thenReturn(testUserB);
        when(groupQueryService.getGroupById(anyLong())).thenReturn(groupRoom);

        GroupInviteMessageResponse response = groupInviteService.inviteGroupRoom(UserMapper.toUserInfo(testUserA), groupInviteRequest);


        assertNotNull(response);
    }

    @Test
    public void 하나의그룹_으로부터_초대를_받는다() {
        List<GroupInviteMessageResponse> invitations = groupInviteService.getInvitations(UserMapper.toUserInfo(testUserA));
        assertEquals(1, invitations.size());
    }
}
