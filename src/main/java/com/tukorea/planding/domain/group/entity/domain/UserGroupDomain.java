package com.tukorea.planding.domain.group.entity.domain;


import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.user.entity.UserDomain;

public class UserGroupDomain {
    private Long id;
    private UserDomain user;

    private GroupRoomDomain groupRoom;

    private boolean isConnected;

}
