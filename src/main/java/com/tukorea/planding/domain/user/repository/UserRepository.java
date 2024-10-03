package com.tukorea.planding.domain.user.repository;

import com.tukorea.planding.domain.user.entity.SocialType;
import com.tukorea.planding.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserCode(String userCode);

    Optional<User> findByEmail(String email);

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    Optional<User> getUserById(Long userId);

    boolean existsByUserCode(String userCode);
}
