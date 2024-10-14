package com.tukorea.planding.domain.auth.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.hibernate.validator.internal.engine.messageinterpolation.el.RootResolver.FORMATTER;

@Repository
@RequiredArgsConstructor
public class TokenInfoCacheRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final int MAXIMUM_REFRESH_TOKEN_EXPIRES_IN_DAY = 30;
    private static final String ISSUE_TIME_KEY_PREFIX = "token:issueTime:"; // Redis에 저장할 키 prefix
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 캐시에 토큰 저장
     *
     * @param key      캐시에 저장할 키, refresh-token String
     * @param userCode 저장할 사용자 정보(유저코드)
     */
    public void save(final String key, final String userCode) {
        redisTemplate.opsForValue().set(
                key,
                userCode,
                MAXIMUM_REFRESH_TOKEN_EXPIRES_IN_DAY,
                TimeUnit.DAYS
        );

    }

    /**
     * 캐시에서 토큰 삭제
     *
     * @param key 삭제할 키, refresh-token String
     */
    public void delete(final String key) {
        redisTemplate.delete(key);
    }


    /**
     * 발급 시간을 Redis에 저장합니다.
     * @param userCode 유저 코드
     * @param issueTime 발급 시간
     */
    public void saveIssueTime(String userCode, LocalDateTime issueTime) {
        String key = ISSUE_TIME_KEY_PREFIX + userCode;
        redisTemplate.opsForValue().set(key, issueTime.format(FORMATTER), 24, TimeUnit.HOURS);
    }

    /**
     * Redis에서 발급 시간을 조회합니다.
     * @param userCode 유저 코드
     * @return 발급 시간 (없으면 null)
     */
    public LocalDateTime getIssueTime(String userCode) {
        String key = ISSUE_TIME_KEY_PREFIX + userCode;
        String issueTimeStr = (String) redisTemplate.opsForValue().get(key);
        return issueTimeStr != null ? LocalDateTime.parse(issueTimeStr, FORMATTER) : null;
    }
}
