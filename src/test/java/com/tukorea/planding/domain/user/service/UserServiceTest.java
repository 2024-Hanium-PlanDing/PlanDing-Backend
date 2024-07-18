package com.tukorea.planding.domain.user.service;

import com.tukorea.planding.domain.group.service.RedisGroupInviteService;
import com.tukorea.planding.domain.user.dto.ProfileResponse;
import com.tukorea.planding.domain.user.entity.SocialType;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.mapper.UserMapper;
import com.tukorea.planding.global.oauth.details.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Transactional
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserQueryService userQueryService;

    @Mock
    private RedisGroupInviteService redisGroupInviteService;

    private User testUserA;


    @BeforeEach
    void setUp() {
        testUserA = new User("test", "image", "username", Role.USER, SocialType.KAKAO, "socialId", "#test");
        ReflectionTestUtils.setField(testUserA, "id", 1L);
    }

    @Test
    void 유저_프로필_조회_성공() {
        //when
        when(userQueryService.getUserProfile(anyLong())).thenReturn(testUserA);

        //then
        ProfileResponse result = userService.getProfile(UserMapper.toUserInfo(testUserA));

        //given
        Assertions.assertNotNull(result);
        Assertions.assertEquals(testUserA.getUserCode(), result.userCode());
        Assertions.assertEquals(Role.USER, result.role());
        Assertions.assertEquals(0, result.groupFavorite());
    }

    @Test
    void 중복코드_생성시_유저코드_재생성() {
        when(userQueryService.existsByUserCode(anyString()))
                .thenReturn(true)
                .thenReturn(false);

        String uniqueUserCode = userService.generateUniqueUserCode();

        assertNotNull(uniqueUserCode);
        assertTrue(uniqueUserCode.startsWith("#"));
        System.out.println(uniqueUserCode);
    }

}