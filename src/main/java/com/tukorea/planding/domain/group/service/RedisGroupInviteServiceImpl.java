package com.tukorea.planding.domain.group.service;

import com.tukorea.planding.common.JsonConverter;
import com.tukorea.planding.domain.group.dto.response.GroupInviteMessageResponse;
import com.tukorea.planding.domain.group.service.port.RedisGroupInviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisGroupInviteServiceImpl implements RedisGroupInviteService {

    private final StringRedisTemplate redisTemplate;
    private final JsonConverter jsonConverter;

    // 초대 생성
    public void createInvitation(String userCode, GroupInviteMessageResponse inviteDTO) {
        String key = "userInvites:" + userCode; // 유저별 키
        String field = inviteDTO.getInviteCode(); // 초대 코드를 필드로 사용
        String value = jsonConverter.convertObjectToJson(inviteDTO); // 초대 정보를 JSON 문자열로 변환

        if (value != null) {
            redisTemplate.opsForHash().put(key, field, value);
            redisTemplate.expire(key, 3, TimeUnit.HOURS);
        }
    }

    // 유저별 모든 초대 조회
    public List<GroupInviteMessageResponse> getAllInvitations(String userCode) {
        String key = "userInvites:" + userCode;
        List<Object> values = redisTemplate.opsForHash().values(key);
        return values.stream()
                .map(value -> jsonConverter.convertJsonToObject((String) value, GroupInviteMessageResponse.class))
                .collect(Collectors.toList());
    }

    // 특정 초대 삭제
    public void deleteInvitation(String userCode, String inviteCode) {
        String key = "userInvites:" + userCode;
        redisTemplate.opsForHash().delete(key, inviteCode);
    }
}
