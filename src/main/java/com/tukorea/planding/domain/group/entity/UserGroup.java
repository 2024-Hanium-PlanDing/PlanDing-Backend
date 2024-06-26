package com.tukorea.planding.domain.group.entity;

import com.tukorea.planding.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_room_id")
    private GroupRoom groupRoom;

    @Setter
    @Column(name = "is_connected")
    private boolean isConnected;

    private UserGroup(User user, GroupRoom groupRoom, boolean isConnected) {
        this.user = user;
        this.groupRoom = groupRoom;
        this.isConnected = isConnected;
    }

    public static UserGroup createUserGroup(User user, GroupRoom groupRoom) {
        return new UserGroup(user, groupRoom, false);
    }
}