package com.tukorea.planding.domain.user.repository;

import com.tukorea.planding.domain.user.entity.SocialType;
import com.tukorea.planding.domain.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserCode(String userCode);

    Optional<User> findByEmail(String email);

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    Optional<User> getUserById(Long userId);

    boolean existsByUserCode(String userCode);

    @Query("SELECT u FROM User u " +
            "JOIN u.userGroup ug " +
            "JOIN ug.groupRoom gr " +
            "WHERE gr.groupCode = :groupCode")
    List<User> findByUserGroupGroupCode(@Param("groupCode") String groupCode);
}
