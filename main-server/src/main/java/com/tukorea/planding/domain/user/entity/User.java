package com.tukorea.planding.domain.user.entity;

import com.tukorea.planding.domain.group.entity.GroupFavorite;
import com.tukorea.planding.domain.group.entity.UserGroup;
import com.tukorea.planding.global.audit.BaseEntity;
import com.tukorea.planding.global.oauth.details.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<UserGroup> userGroup = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<GroupFavorite> groupFavorites = new ArrayList<>();

    @Builder
    public User(String email, String profileImage, String username, Role role, SocialType socialType, String socialId, String userCode) {
        this.email = email;
        this.profileImage = profileImage;
        this.username = username;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
        this.userCode = userCode;
    }




    public static String createCode() {
        String c = UUID.randomUUID().toString();
        return "#" + c.substring(0, 4);
    }

}
