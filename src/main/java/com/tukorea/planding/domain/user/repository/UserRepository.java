package com.tukorea.planding.domain.user.repository;

import com.tukorea.planding.domain.user.entity.SocialType;
import com.tukorea.planding.domain.user.entity.UserDomain;

import java.util.Optional;

public interface UserRepository {
    Optional<UserDomain> findByUserCode(String userCode);

    Optional<UserDomain> findByEmail(String email);

    Optional<UserDomain> getUserById(Long userId);
    Optional<UserDomain> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    boolean existsByUserCode(String userCode);

    UserDomain save(UserDomain userDomain);
}
