package com.tukorea.planding.domain.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tukorea.planding.common.service.UserCodeHolder;
import com.tukorea.planding.domain.group.service.RedisGroupInviteService;
import com.tukorea.planding.domain.user.dto.ProfileResponse;
import com.tukorea.planding.domain.user.dto.UserResponse;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.global.oauth.details.Role;
import com.tukorea.planding.mock.FakeUserRepository;
import com.tukorea.planding.mock.TestUserCodeHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;


class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void init() {
        UserCodeHolder testUserCodeHolder = new TestUserCodeHolder("1234");
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        UserQueryService userQueryService = new UserQueryService(fakeUserRepository);
        UserCodeGenerator userCodeGenerator = new UserCodeGenerator(userQueryService, testUserCodeHolder);


        ObjectMapper objectMapper = new ObjectMapper();
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        RedisGroupInviteService redisGroupInviteService = new RedisGroupInviteService(redisTemplate, objectMapper);
        this.userService = UserService.builder()
                .userCodeGenerator(userCodeGenerator)
                .redisGroupInviteService(redisGroupInviteService)
                .userQueryService(userQueryService)
                .build();

        fakeUserRepository.save(UserDomain.builder()
                .id(1L)
                .email("ksu9541@tukorea.ac.kr")
                .username("ksu9541")
                .userCode("1234")
                .alarm(true)
                .role(Role.USER)
                .build());

    }


    @Test
    void 안드로이드_유저를_생성할_수_있다() {

    }

    @Test
    void User의_프로필_을_조회할_수_있다() {
        ProfileResponse profile = userService.getProfile(1L);

        assertThat(profile.userCode()).isEqualTo("1234");
        assertThat(profile.email()).isEqualTo("ksu9541@tukorea.ac.kr");
    }

    @Test
    void User코드를_통해_FCM_토큰을_조회할_수_있다() {

    }


}