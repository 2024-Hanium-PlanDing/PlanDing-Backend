package com.tukorea.planding.global;

import com.tukorea.planding.domain.auth.repository.TokenInfoCacheRepository;
import com.tukorea.planding.global.config.security.jwt.JwtTokenHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserLogoutHandler implements LogoutHandler {

    private final JwtTokenHandler jwtTokenHandler;
    private final TokenInfoCacheRepository tokenInfoCacheRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String refreshToken = jwtTokenHandler.extractRefreshToken(request).orElse(null);
        tokenInfoCacheRepository.delete(refreshToken);
        log.info("로그아웃 성공");
    }
}
