package com.tukorea.planding.domain.user.service;

import com.tukorea.planding.domain.user.dto.UserResponse;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.domain.user.repository.UserRepository;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;

    public UserDomain save(UserDomain userDomain) {
        return userRepository.save(User.fromModel(userDomain).toModel());
    }

    @Transactional(readOnly = true)
    public UserDomain getUserByUserCode(String userCode) {
        return userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    // 유저 정보를 조회하는 메서드에 캐싱을 적용
    @Cacheable(value = "userInfoCache", key = "#userCode")
    public UserResponse getUserInfo(String userCode) {
        UserDomain user = getUserByUserCode(userCode);
        return UserResponse.toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserDomain getUserProfile(Long userId) {
        return userRepository.getUserById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public UserDomain findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Transactional(readOnly = true)
    public boolean existsByUserCode(String userCode) {
        return userRepository.existsByUserCode(userCode);
    }
}
