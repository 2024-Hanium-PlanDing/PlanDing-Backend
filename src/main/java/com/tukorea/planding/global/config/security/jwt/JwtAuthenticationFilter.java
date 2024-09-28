package com.tukorea.planding.global.config.security.jwt;


import com.tukorea.planding.domain.auth.repository.TokenInfoCacheRepository;
import com.tukorea.planding.domain.auth.service.TokenService;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.repository.UserRepository;
import com.tukorea.planding.domain.user.service.UserQueryService;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


/**
 * RTR 방식 사용 프론트에서 액세스토큰의 만료여부를 확인하고 수행되는 로직
 * <p>
 * 1. (일반적인 상황) 서버 request 헤더에 access-token이 보내어짐
 * 2. (액세스토큰이 만료되었을 경우) 프론트에서 만료여부를 확인했을 때, access-token이 만료되었을때 request 헤더에는
 * refresh-token이 담겨서 보내어짐
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenHandler jwtTokenHandler;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private final TokenInfoCacheRepository tokenInfoCacheRepository;
    private final UserQueryService userQueryService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String refreshToken = jwtTokenHandler.extractRefreshToken(request)
                .filter(jwtTokenHandler::validateToken)
                .orElse(null);

        if (refreshToken != null) {
            log.info("refresh: {}",refreshToken);
            checkRefreshTokenAndReIssueAccessToken(refreshToken, response);
            return;
        }

        // access-token이 만료되지 않았을때 수행되는 로직
        checkAccessTokenAndAuthentication(request, response, filterChain);

    }

    private Authentication getAuthentication(UserInfo userInfo) {
        return new UsernamePasswordAuthenticationToken(userInfo, null, List.of(new SimpleGrantedAuthority(userInfo.getRole().getAuthority())));
    }

    /**
     * 액세스토큰 검증 및 시큐리티 컨텍스트에 유저 등록
     * RTR 방식으로 프론트에서 액세스토큰의 만료여부를 확인하고 헤더에 보내어지기 때문에, 여기서는 단순히 액세스토큰의 오류를 검증
     * <p>
     * 1. (검증 성공시) email로 부터 유저정보를 가져와 SecurityContext에 등록
     * 2. (검증 실패시) 에러 발생
     *
     * @param request
     */
    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("Access 토큰 확인 및 검증");
        String accessToken = jwtTokenHandler.extractAccessToken(request)
                .filter(jwtTokenHandler::validateToken)
                .orElse(null);

        String userCode = jwtTokenHandler.extractClaim(accessToken, claims -> claims.get("code", String.class));

        //TODO: 영속성 컨텍스트 1차 캐시, 2차 캐시가 적용되지않는이유
        saveAuthentication(userQueryService.getUserInfo(userCode));


        filterChain.doFilter(request, response);
    }


    /**
     * 이 로직이 수행되는 순간 AccessToken은 만료된 상황
     * <p>
     * RefreshToken cache 저장 ->  (key,value) : ( 'email', 'refreshToken')
     *
     * @param refreshToken
     * @param response
     * @return 새로 발급한 AccessToken, RefreshToken을 response 헤더에 저장
     */
    private void checkRefreshTokenAndReIssueAccessToken(
            String refreshToken,
            HttpServletResponse response) {
        log.debug("Refresh 토큰 확인 및 검증");
        if (jwtTokenHandler.isRefreshTokenExpired(refreshToken)) {
            log.error("리프레시 토큰 만료");
            tokenInfoCacheRepository.delete(refreshToken);
            throw new BusinessException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        String newAccessToken = tokenService.reIssueAccessToken(refreshToken);
        jwtUtil.sendAccessAndRefreshToken(response, newAccessToken, refreshToken);
    }

    private void saveAuthentication(UserInfo userInfo) {
        Authentication authentication = getAuthentication(userInfo);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String path = request.getRequestURI();

        String[] excludePath = {
                "/api/v1/temporary-token",
                "/api/v1/ws/**",
                "/api/v1/login/**",
                "/v3/api-docs/**",
                "/api-docs/json/**",
                "/swagger-ui/**",
                "/api/v1/health",
                "/api/v1/chatbot",
                "/actuator/**"
        };

        return Arrays.stream(excludePath).anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
}
