package com.tukorea.planding.domain.group.entity;

import com.tukorea.planding.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGroupMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_room_id")
    private GroupRoom groupRoom;

    @Setter
    @Column(name = "is_connected")
    private boolean isConnected;

    @Builder
    public UserGroupMembership(User user, GroupRoom groupRoom, boolean isConnected) {
        this.user = user;
        this.groupRoom = groupRoom;
        this.isConnected = isConnected;
    }


}