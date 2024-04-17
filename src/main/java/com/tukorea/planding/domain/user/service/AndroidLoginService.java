package com.tukorea.planding.domain.user.service;

import com.tukorea.planding.domain.user.dto.AndroidLoginRequest;
import com.tukorea.planding.domain.user.dto.AndroidLoginResponse;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.mapper.UserMapper;
import com.tukorea.planding.global.config.security.jwt.JwtTokenHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AndroidLoginService {

    private final JwtTokenHandler jwtTokenHandler;
    private final UserService userService;

    public AndroidLoginResponse signupApp(AndroidLoginRequest androidLoginRequest) {
        User user = userService.createUserFromRequest(androidLoginRequest);
        String accessToken = jwtTokenHandler.generateAccessToken(user.getId(), user.getUserCode());
        String refreshToken = jwtTokenHandler.generateRefreshToken(user.getId(), user.getUserCode());
        return UserMapper.toAndroidLoginResponse(user, accessToken, refreshToken);
    }
}
