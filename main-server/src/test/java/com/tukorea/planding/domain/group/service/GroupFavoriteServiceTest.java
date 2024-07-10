package com.tukorea.planding.domain.group.service;

import com.tukorea.planding.domain.group.dto.response.GroupFavoriteResponse;
import com.tukorea.planding.domain.group.entity.GroupFavorite;
import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.repository.favorite.GroupFavoriteRepository;
import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.user.entity.SocialType;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.mapper.UserMapper;
import com.tukorea.planding.domain.user.service.UserQueryService;
import com.tukorea.planding.global.oauth.details.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Transactional
class GroupFavoriteServiceTest {

    @Mock
    private GroupFavoriteRepository groupFavoriteRepository;

    @Mock
    private UserQueryService userQueryService;

    @Mock
    private GroupQueryService groupQueryService;

    @InjectMocks
    private GroupFavoriteService groupFavoriteService;

    private User user;
    private GroupRoom groupRoom;

    @BeforeEach
    public void setUp() {
        user = new User("test", "profile", "username", Role.USER, SocialType.KAKAO, null, "#testA"); // 테스트용 사용자 정보 초기화
        groupRoom = new GroupRoom("name", null, user.getUserCode(), "Gcode");
        ReflectionTestUtils.setField(groupRoom, "id", 1L);
        ReflectionTestUtils.setField(user, "id", 1L);
    }

    @Test
    @DisplayName("성공: 즐겨찾기 그룹 등록")
    public void test() {
        GroupFavorite groupFavorite = GroupFavorite.createGroupFavorite(user, groupRoom);

        when(userQueryService.getUserByUserCode(anyString())).thenReturn(user);
        when(groupQueryService.getGroupById(anyLong())).thenReturn(groupRoom);
        when(groupFavoriteRepository.save(any(GroupFavorite.class))).thenReturn(groupFavorite);

        GroupFavoriteResponse response = groupFavoriteService.addFavorite(UserMapper.toUserInfo(user), 1L);

        assertNotNull(response);
        assertNotNull(response.groupName());
    }

    @Test
    @DisplayName("즐겨찾기 해제")
    public void deleteTest() {
        groupFavoriteService.deleteFavorite(UserMapper.toUserInfo(user), groupRoom.getGroupCode());
        verify(groupFavoriteRepository).deleteByUserIdAndGroupRoomId(anyLong(), anyString());
    }
}
