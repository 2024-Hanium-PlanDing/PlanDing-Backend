package com.tukorea.planding.domain.user.repository;

import com.tukorea.planding.domain.user.dto.UserResponse;
import com.tukorea.planding.domain.user.entity.SocialType;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<UserDomain> findByUserCode(String userCode) {
        return userJpaRepository.findByUserCode(userCode).map(User::toModel);
    }

    @Override
    public Optional<UserDomain> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(User::toModel);
    }

    @Override
    public Optional<UserDomain> getUserById(Long userId) {
        return userJpaRepository.getUserById(userId).map(User::toModel);
    }

    @Override
    public Optional<UserDomain> findBySocialTypeAndSocialId(SocialType socialType, String socialId) {
        return userJpaRepository.findBySocialTypeAndSocialId(socialType, socialId).map(User::toModel);
    }

    @Override
    public boolean existsByUserCode(String userCode) {
        return userJpaRepository.existsByUserCode(userCode);
    }


    @Override
    public UserDomain save(UserDomain userDomain) {
        return userJpaRepository.save(User.fromModel(userDomain)).toModel();
    }

    @Override
    public List<UserDomain> findByUserGroupGroupCode(String groupCode) {
        return userJpaRepository.findByUserGroupGroupCode(groupCode).stream()
                .map(User::toModel)
                .collect(Collectors.toList());
    }
}
