package com.tukorea.planding.domain.group.entity;

import com.tukorea.planding.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private GroupRoom groupRoom;

    private GroupFavorite(User user, GroupRoom groupRoom) {
        this.user = user;
        this.groupRoom = groupRoom;
    }

    public static GroupFavorite createGroupFavorite(User user, GroupRoom groupRoom) {
        return new GroupFavorite(user, groupRoom);
    }
}
