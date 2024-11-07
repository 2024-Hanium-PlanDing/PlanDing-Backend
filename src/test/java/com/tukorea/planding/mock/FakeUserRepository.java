package com.tukorea.planding.mock;

import com.tukorea.planding.domain.user.entity.SocialType;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FakeUserRepository implements UserRepository {
    private Long count = 0L;
    private final List<UserDomain> data = new ArrayList<>();

    @Override
    public Optional<UserDomain> findByUserCode(String userCode) {
        return data.stream().filter(item -> item.getUserCode().equals(userCode)).findAny();
    }

    @Override
    public Optional<UserDomain> findByEmail(String email) {
        return data.stream().filter(item -> item.getEmail().equals(email)).findAny();
    }

    @Override
    public Optional<UserDomain> getUserById(Long userId) {
        return data.stream().filter(item -> item.getId().equals(userId)).findAny();
    }

    @Override
    public Optional<UserDomain> findBySocialTypeAndSocialId(SocialType socialType, String socialId) {
        return data.stream()
                .filter(user -> user.getSocialType().equals(socialType) && user.getSocialId().equals(socialId))
                .findFirst();
    }

    @Override
    public boolean existsByUserCode(String userCode) {
        return data.stream()
                .anyMatch(user -> user.getUserCode().equals(userCode));
    }

    @Override
    public UserDomain save(UserDomain userDomain) {
        if (userDomain.getId() == null || userDomain.getId() == 0) {
            UserDomain newUser = UserDomain.builder()
                    .id(count++)
                    .email(userDomain.getEmail())
                    .userCode(userDomain.getUserCode())
                    .username(userDomain.getUsername())
                    .role(userDomain.getRole())
                    .alarm(userDomain.isAlarm())
                    .fcmToken(userDomain.getFcmToken())
                    .profileImage(userDomain.getProfileImage())
                    .socialId(userDomain.getSocialId())
                    .socialType(userDomain.getSocialType())
                    .build();
            data.add(newUser);
            return newUser;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), userDomain.getId()));
            data.add(userDomain);
            return userDomain;
        }
    }
}
