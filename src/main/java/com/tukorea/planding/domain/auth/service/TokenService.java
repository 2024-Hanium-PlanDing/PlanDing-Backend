package com.tukorea.planding.domain.auth.service;

import com.tukorea.planding.domain.auth.dto.TemporaryTokenResponse;
import com.tukorea.planding.domain.auth.dto.TokenResponse;
import com.tukorea.planding.domain.auth.repository.TokenInfoCacheRepository;
import com.tukorea.planding.global.config.security.jwt.JwtTokenHandler;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {


    private final JwtTokenHandler jwtTokenHandler;
    private final TokenInfoCacheRepository tokenInfoCacheRepository;

    /**
     * 임시 토큰을 이용하여 새로운 액세스 및 리프레시 토큰 발급
     *
     * @param temporaryToken 임시 토큰
     * @return 새로운 토큰 정보
     */
    public TokenResponse issueTokenFromTemporaryToken(final String temporaryToken) {
        // 임시 토큰에서 userId 추출
        String userCode = jwtTokenHandler.extractClaim(temporaryToken, claims -> claims.get("code", String.class));
        Long userId = jwtTokenHandler.extractClaim(temporaryToken, claims -> claims.get("id", Long.class));
        // 새로운 토큰 생성
        return createNewToken(userId, userCode);
    }


    public TokenResponse createNewToken(final Long userId, final String userCode) {
        TokenResponse tokenResponse = createTokenResponse(userId, userCode);
        tokenInfoCacheRepository.save(tokenResponse.refreshToken(), userCode);
        tokenInfoCacheRepository.saveIssueTime(userCode, LocalDateTime.now()); // 토큰 발급시간관리
        return tokenResponse;
    }

    public String reIssueAccessToken(final String refreshToken) {
        // TODO: fake_id,fake_userCode로 수정할것
        String userCode = jwtTokenHandler.extractClaim(refreshToken, claims -> claims.get("code", String.class));
        Long userId = jwtTokenHandler.extractClaim(refreshToken, claims -> claims.get("id", Long.class));

        // if 리프레시 토큰이 만료되지않았는데, 발급하는것이라면 에러 throw
        LocalDateTime issuedAt = tokenInfoCacheRepository.getIssueTime(userCode);
        if (issuedAt != null && !jwtTokenHandler.isReissueAllowed(issuedAt)) {
            throw new BusinessException(ErrorCode.EXPIRED_REFRESH_TOKEN_ERROR);
        }


        return jwtTokenHandler.generateAccessToken(userId, userCode); // new access-token 생성
    }

    public TemporaryTokenResponse createTemporaryTokenResponse(final Long userId,final String userCode) {
        return new TemporaryTokenResponse(
                jwtTokenHandler.generateTemporaryToken(userId,userCode)
        );
    }

    private TokenResponse createTokenResponse(final Long userId, final String userCode) {
        return new TokenResponse(
                jwtTokenHandler.generateAccessToken(userId, userCode),
                jwtTokenHandler.generateRefreshToken(userId, userCode)
        );
    }

}
