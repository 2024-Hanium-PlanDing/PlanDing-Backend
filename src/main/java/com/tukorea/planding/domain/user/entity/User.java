package com.tukorea.planding.domain.user.entity;

import com.tukorea.planding.domain.group.entity.GroupFavorite;
import com.tukorea.planding.domain.group.entity.UserGroup;
import com.tukorea.planding.global.audit.BaseEntity;
import com.tukorea.planding.global.oauth.details.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "username")
    private String username;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "social_type")
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "social_id")
    private String socialId;

    @Column(name = "user_code", nullable = false, unique = true)
    private String userCode;

    @Column(name = "alarm")
    private boolean alarm = true;

    @Column(name = "fcm_token")
    private String fcmToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<UserGroup> userGroup = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<GroupFavorite> groupFavorites = new ArrayList<>();

    @Builder
    public User(String email, String profileImage, String username, Role role, SocialType socialType, String socialId, String userCode, String fcmToken) {
        this.email = email;
        this.profileImage = profileImage;
        this.username = username;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
        this.userCode = userCode;
        this.fcmToken = fcmToken;
    }

    public static User fromModel(UserDomain userDomain){
        return User.builder()
                .socialType(userDomain.getSocialType())
                .socialId(userDomain.getSocialId())
                .profileImage(userDomain.getProfileImage())
                .fcmToken(userDomain.getFcmToken())
                .email(userDomain.getEmail())
                .role(userDomain.getRole())
                .username(userDomain.getUsername())
                .userCode(userDomain.getUserCode())
                .build();
    }

    public UserDomain toModel(){
        return UserDomain.builder()
                .id(id)
                .email(email)
                .profileImage(profileImage)
                .username(username)
                .userCode(userCode)
                .socialId(socialId)
                .socialType(socialType)
                .fcmToken(fcmToken)
                .alarm(alarm)
                .build();
    }
}
