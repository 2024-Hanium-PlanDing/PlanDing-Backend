package com.tukorea.planding.global.config.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    /**
     * 토큰을 헤더 응답에 포함
     *
     * @param response     응답 설정
     * @param accessToken  액세스 토큰
     * @param refreshToken 리프레스 토큰
     * @return 유효한 토큰이면 {@code true}
     */
    public void sendAccessAndRefreshToken(HttpServletResponse response,
                                          String accessToken,
                                          String refreshToken
    ) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
    }

    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        log.info("Access Token 헤더 설정");
        response.setHeader(jwtProperties.getAccessHeader(), JwtConstant.BEARER.getValue() + accessToken);
    }

    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        log.info("Refresh Token 헤더 설정");
        response.setHeader(jwtProperties.getRefreshHeader(), JwtConstant.BEARER.getValue() + refreshToken);
    }

    //TODO: https 배포시 수정
    public Cookie setAccessTokenCookie(String accessToken) {
        Cookie accessTokenCookie = new Cookie(jwtProperties.getAccessHeader(), accessToken);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(false); // HttpOnly를 false로 설정해서 프론트에서 접근 가능하도록 함
        accessTokenCookie.setMaxAge((int) jwtProperties.getAccessExpiration()); // 1시간 유효
        return accessTokenCookie;
    }

    public Cookie setRefreshTokenCookie(String refreshToken) {
        Cookie refreshTokenCookie = new Cookie(jwtProperties.getRefreshHeader(), refreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(false); // HttpOnly를 false로 설정
        refreshTokenCookie.setMaxAge((int) jwtProperties.getRefreshExpiration());
        return refreshTokenCookie;
    }

    public void setTokensInResponse(HttpServletResponse response, String accessToken, String refreshToken) {
        Cookie accessTokenCookie = setAccessTokenCookie(accessToken);
        Cookie refreshTokenCookie = setRefreshTokenCookie(refreshToken);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }
}
