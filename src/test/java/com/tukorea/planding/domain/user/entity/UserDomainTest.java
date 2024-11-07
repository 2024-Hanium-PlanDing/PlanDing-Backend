package com.tukorea.planding.domain.user.entity;

import com.tukorea.planding.common.service.UserCodeHolder;
import com.tukorea.planding.domain.user.dto.AndroidLoginResponse;
import com.tukorea.planding.global.oauth.details.Role;
import com.tukorea.planding.mock.TestUserCodeHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDomainTest {

    @Test
    void User를_생성_할_수_있다() {
        UserCodeHolder userCodeHolder=new TestUserCodeHolder("1234");
        UserDomain userDomain = UserDomain.builder()
                .id(1L)
                .email("ksu9541@tukorea.ac.kr")
                .username("ksu9541")
                .socialId("1")
                .socialType(SocialType.KAKAO)
                .profileImage("https://naver.com")
                .alarm(true)
                .userCode(userCodeHolder.userCode())
                .role(Role.USER)
                .build();

        User user = User.fromModel(userDomain);

        assertThat(user.getEmail()).isEqualTo("ksu9541@tukorea.ac.kr");
        assertThat(user.getUsername()).isEqualTo("ksu9541");
        assertThat(user.getUserCode()).isEqualTo("1234");
    }

    @Test
    void User는_fcmToken_필드를_업데이트_할_수_있다() {
        UserDomain userDomain = UserDomain.builder()
                .id(1L)
                .email("ksu9541@tukorea.ac.kr")
                .username("ksu9541")
                .socialId("1")
                .socialType(SocialType.KAKAO)
                .profileImage("https://naver.com")
                .alarm(true)
                .role(Role.USER)
                .fcmToken("aaaa-aaaa-aaaa-aaaa")
                .build();

        userDomain = userDomain.updateFcmToken("bbbb-bbbb-bbbb");

        assertThat(userDomain.getFcmToken()).isEqualTo("bbbb-bbbb-bbbb");
    }

    @Test
    void User는_alarm_필드를_업데이트_할_수_있다() {
        UserDomain userDomain = UserDomain.builder()
                .id(1L)
                .email("ksu9541@tukorea.ac.kr")
                .username("ksu9541")
                .socialId("1")
                .socialType(SocialType.KAKAO)
                .profileImage("https://naver.com")
                .alarm(true)
                .role(Role.USER)
                .fcmToken("aaaa-aaaa-aaaa-aaaa")
                .build();

        userDomain = userDomain.updateAlarm(false);

        assertThat(userDomain.isAlarm()).isEqualTo(false);
    }


    @Test
    void User는_Android_객체로_수정할_수_있다() {
        UserDomain userDomain = UserDomain.builder()
                .id(1L)
                .email("ksu9541@tukorea.ac.kr")
                .username("ksu9541")
                .socialId("1")
                .socialType(SocialType.KAKAO)
                .profileImage("https://naver.com")
                .alarm(true)
                .role(Role.USER)
                .fcmToken("aaaa-aaaa-aaaa-aaaa")
                .build();

        String accessToken="accessToken";
        String refreshToken="refreshToken";

        AndroidLoginResponse response=AndroidLoginResponse.toAndroidLoginResponse(userDomain,accessToken,refreshToken);

        assertThat(response.getAccessToken()).isEqualTo("accessToken");
        assertThat(response.getRefreshToken()).isEqualTo("refreshToken");
    }

}