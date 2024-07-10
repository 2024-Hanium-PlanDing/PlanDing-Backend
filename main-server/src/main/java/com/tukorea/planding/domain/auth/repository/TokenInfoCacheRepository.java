package com.tukorea.planding.domain.auth.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class TokenInfoCacheRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final int MAXIMUM_REFRESH_TOKEN_EXPIRES_IN_DAY = 30;

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

    public void rename(
            final String oldKey,
            final String newKey
    ) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 캐시로부터 유저정보 가져오기
     *
     * @param infoKey 캐시로 부터 가져올 키, refresh-token String
     */
    public Optional<String> getUserInfo(final String infoKey) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(infoKey).toString());
    }
}
