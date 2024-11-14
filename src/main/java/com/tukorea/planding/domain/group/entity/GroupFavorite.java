package com.tukorea.planding.domain.group.entity;

import com.tukorea.planding.domain.group.entity.domain.GroupFavoriteDomain;
import com.tukorea.planding.domain.group.entity.domain.GroupRoomDomain;
import com.tukorea.planding.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder
    public GroupFavorite(Long id, User user, GroupRoom groupRoom) {
        this.id = id;
        this.user = user;
        this.groupRoom = groupRoom;
    }

    public GroupFavoriteDomain toModel() {
        return GroupFavoriteDomain.builder()
                .id(id)
                .groupRoomDomain(groupRoom.toModel())
                .userDomain(user.toModel())
                .build();
    }

    public static GroupFavorite fromModel(GroupFavoriteDomain groupFavoriteDomain) {
        return GroupFavorite.builder()
                .id(groupFavoriteDomain.getId())
                .groupRoom(GroupRoom.fromModel(groupFavoriteDomain.getGroupRoomDomain()))
                .user(User.fromModel(groupFavoriteDomain.getUserDomain()))
                .build();
    }
}
