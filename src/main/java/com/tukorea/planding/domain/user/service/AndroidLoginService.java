package com.tukorea.planding.domain.user.service;

import com.tukorea.planding.domain.user.dto.AndroidLoginRequest;
import com.tukorea.planding.domain.user.dto.AndroidLoginResponse;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.global.config.security.jwt.JwtTokenHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AndroidLoginService {

    private final JwtTokenHandler jwtTokenHandler;
    private final UserService userService;
    private final UserQueryService userQueryService;
    private final UserCodeGeneratorImpl userCodeGenerator;

    public AndroidLoginResponse signupApp(AndroidLoginRequest androidLoginRequest) {

        UserDomain userDomain = userQueryService.findByEmail(androidLoginRequest.accountEmail());

        if (userDomain == null) {
            // 유저가 존재하지 않으면 회원가입
            userDomain = userService.createAndroid(androidLoginRequest);
        }

        String accessToken = jwtTokenHandler.generateAccessToken(userDomain.getId(), userDomain.getUserCode());
        String refreshToken = jwtTokenHandler.generateRefreshToken(userDomain.getId(), userDomain.getUserCode());

        return AndroidLoginResponse.toAndroidLoginResponse(userDomain, accessToken, refreshToken);
    }
}
