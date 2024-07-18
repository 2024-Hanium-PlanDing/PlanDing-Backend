package com.tukorea.planding.domain.auth.service;

import com.tukorea.planding.domain.auth.dto.TokenResponse;
import com.tukorea.planding.domain.auth.repository.TokenInfoCacheRepository;
import com.tukorea.planding.global.config.security.jwt.JwtTokenHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {


    private final JwtTokenHandler jwtTokenHandler;
    private final TokenInfoCacheRepository tokenInfoCacheRepository;


    public TokenResponse createNewToken(final Long userId, final String userCode) {
        TokenResponse tokenResponse = createTokenResponse(userId, userCode);
        tokenInfoCacheRepository.save(tokenResponse.refreshToken(), userCode);
        return tokenResponse;
    }

    public TokenResponse reIssueToken(final String refreshToken) {
        String userCode = jwtTokenHandler.extractClaim(refreshToken, claims -> claims.get("code", String.class));
        Long userId = jwtTokenHandler.extractClaim(refreshToken, claims -> claims.get("id", Long.class));

        String newRefreshToken = jwtTokenHandler.generateRefreshToken(userId, userCode);
        tokenInfoCacheRepository.rename(refreshToken, newRefreshToken);

        return createTokenResponse(userId, userCode);
    }

    private TokenResponse createTokenResponse(final Long userId, final String userCode) {
        return new TokenResponse(
                jwtTokenHandler.generateAccessToken(userId, userCode),
                jwtTokenHandler.generateRefreshToken(userId, userCode)
        );
    }
}
