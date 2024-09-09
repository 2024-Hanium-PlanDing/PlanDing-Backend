package com.tukorea.planding.global.config.security.jwt;


import com.tukorea.planding.domain.auth.dto.TemporaryTokenResponse;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenHandler {

    private final JwtProperties jwtProperties;

    /**
     * 토큰 생성 메서드
     *
     * @param expiration 만료시간 (액세스 or 리프레시)
     * @param userId     사용자 유저PK
     * @param userCode   사용자 유저코드
     * @return 토큰
     */
    public String generateToken(long expiration, final Long userId, final String userCode) {
        return Jwts.builder()
                .setClaims(createClaims(userId, userCode))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSECRET()) // HS512과 비교했을때 본 서비스는 암호화 난이도를 낮게 설정
                .compact();
    }

    public String generateAccessToken(final Long userId, final String userCode) {
        return generateToken(jwtProperties.getAccessExpiration(), userId, userCode);
    }

    public String generateRefreshToken(final Long userId, final String userCode) {
        return generateToken(jwtProperties.getRefreshExpiration(), userId, userCode);
    }

    // 임시토큰 생성
    public String generateTemporaryToken(final Long userId,final String userCode) {
        return Jwts.builder()
                .setClaims(createClaims(userId,userCode))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 10 * 1000)) // 10분
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSECRET())
                .compact();
    }

    /**
     * 토큰을 파싱해서 올바른 토큰인지 확인
     *
     * @param token 검증할 토큰
     * @return 유효한 토큰이면 {@code true}
     */
    public boolean validateToken(final String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expired: {}", e.getMessage());
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid token: {}", e.getMessage());
            return false;
        }
    }


    private Map<String, Object> createClaims(final Long userId, final String userCode) { // payload
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userId);
        claims.put("code", userCode);
        return claims;
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(jwtProperties.getSECRET())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(jwtProperties.getRefreshHeader()))
                .filter(refreshToken -> refreshToken.startsWith(JwtConstant.BEARER.getValue()))
                .map(refreshToken -> refreshToken.replace(JwtConstant.BEARER.getValue(), ""));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(jwtProperties.getAccessHeader()))
                .filter(accessToken -> accessToken.startsWith(JwtConstant.BEARER.getValue()))
                .map(accessToken -> accessToken.replace(JwtConstant.BEARER.getValue(), ""));
    }

    // 리프레시 토큰 만료여부
    public boolean isRefreshTokenExpired(String refreshToken) {
        Claims claims = extractAllClaims(refreshToken);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }


}
