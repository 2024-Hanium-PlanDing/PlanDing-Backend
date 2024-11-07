package com.tukorea.planding.domain.user.service;

import com.tukorea.planding.common.service.UserCodeHolder;
import com.tukorea.planding.domain.group.service.port.RedisGroupInviteService;
import com.tukorea.planding.domain.user.dto.AndroidLoginRequest;
import com.tukorea.planding.domain.user.dto.ProfileResponse;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.domain.user.service.port.UserCodeGenerator;
import com.tukorea.planding.global.oauth.details.Role;
import com.tukorea.planding.mock.FakeRedisGroupInviteService;
import com.tukorea.planding.mock.FakeUserRepository;
import com.tukorea.planding.mock.TestUserCodeGenerator;
import com.tukorea.planding.mock.TestUserCodeHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void init() {
        UserCodeHolder testUserCodeHolder = new TestUserCodeHolder("#1234"); // 유저 코드 생성
        UserCodeGenerator userCodeGenerator = new TestUserCodeGenerator(); // 유저코드 생성전 중복되지않는 유저코드 생성하는 역할
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        UserQueryService userQueryService = new UserQueryService(fakeUserRepository);


        RedisGroupInviteService redisGroupInviteService = new FakeRedisGroupInviteService();
        this.userService = UserService.builder()
                .userCodeGenerator(userCodeGenerator)
                .redisGroupInviteService(redisGroupInviteService)
                .userQueryService(userQueryService)
                .build();

        fakeUserRepository.save(UserDomain.builder()
                .id(1L)
                .email("ksu9541@tukorea.ac.kr")
                .username("ksu9541")
                .userCode(testUserCodeHolder.userCode())
                .fcmToken("aaaa-aaaa-aaaa-aaaa")
                .alarm(true)
                .role(Role.USER)
                .build());
    }


    @Test
    void 안드로이드_유저를_생성할_수_있다() {
        AndroidLoginRequest androidLoginRequest = AndroidLoginRequest.builder()
                .accountEmail("test@naver.com")
                .profileNickname("android")
                .socialId("1111")
                .profileImage("https://image")
                .build();
        UserDomain result = userService.createAndroid(androidLoginRequest);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@naver.com");
        assertThat(result.getUserCode()).isEqualTo("1");
    }

    @Test
    void User의_프로필_을_조회할_수_있다() {
        ProfileResponse profile = userService.getProfile(1L);

        assertThat(profile.userCode()).isEqualTo("#1234");
        assertThat(profile.email()).isEqualTo("ksu9541@tukorea.ac.kr");
    }

    @Test
    void User코드를_통해_FCM_토큰을_조회할_수_있다() {
        String fcmToken = userService.getFcmTokenByUserCode("#1234");

        assertThat(fcmToken).isEqualTo("aaaa-aaaa-aaaa-aaaa");
    }


}