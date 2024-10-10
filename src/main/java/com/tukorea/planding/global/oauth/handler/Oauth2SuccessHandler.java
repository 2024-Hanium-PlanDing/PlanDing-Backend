package com.tukorea.planding.global.oauth.handler;

import com.tukorea.planding.domain.auth.dto.TemporaryTokenResponse;
import com.tukorea.planding.domain.auth.service.TokenService;
import com.tukorea.planding.global.config.security.jwt.JwtProperties;
import com.tukorea.planding.global.oauth.service.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final JwtProperties jwtProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("로그인성공");

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        TemporaryTokenResponse temporaryToken = tokenService.createTemporaryTokenResponse(oAuth2User.getUserId(), oAuth2User.getUserCode());

        log.info("임시token생성 ={}", temporaryToken.temporaryToken());

        String url = makeRedirectUrl(temporaryToken.temporaryToken());
        getRedirectStrategy().sendRedirect(request, response, url);
    }

    private String makeRedirectUrl(String temporaryAccessToken) {
        return UriComponentsBuilder.fromUriString(jwtProperties.getRedirectUrl())
                .queryParam("temporary", temporaryAccessToken)
                .build().toUriString();
    }
}
