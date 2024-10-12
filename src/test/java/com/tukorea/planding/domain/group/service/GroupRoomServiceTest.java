package com.tukorea.planding.domain.group.service;

import com.tukorea.planding.domain.group.dto.response.GroupInformationResponse;
import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.entity.UserGroup;
import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.group.service.query.UserGroupQueryService;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.entity.SocialType;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.global.oauth.details.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@Transactional
class GroupRoomServiceTest {

    @InjectMocks
    private GroupRoomService groupRoomService;

    @Mock
    private GroupQueryService groupQueryService;

    @Mock
    private UserGroupQueryService userGroupQueryService;

    private User user;
    private GroupRoom groupRoom;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        user=new User("email","profileImage","username", Role.USER, SocialType.KAKAO,null,"#1234",null);

        groupRoom=new GroupRoom("name","description",user,"G-1234");

        UserGroup userGroup=UserGroup.createUserGroup(user,groupRoom);
        groupRoom.getUserGroups().add(userGroup);
    }

    @Test
    void getGroupUsers_성공_그룹멤버인경우(){
        when(groupQueryService.getGroupByCode("G-1234")).thenReturn(groupRoom);

        GroupInformationResponse response=groupRoomService.getGroupUsers(
                UserInfo.builder()
                        .userCode("#1234")
                        .build(),"G-1234"
        );

        assertThat(response.groupCode()).isEqualTo("G-1234");
        assertThat(response.owner()).isEqualTo("#1234");
        assertThat(response.isGroupAdmin()).isTrue();
    }

}