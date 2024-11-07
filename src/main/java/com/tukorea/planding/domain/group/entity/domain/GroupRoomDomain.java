package com.tukorea.planding.domain.group.entity.domain;

import com.tukorea.planding.domain.group.entity.GroupFavorite;
import com.tukorea.planding.domain.group.entity.UserGroup;
import com.tukorea.planding.domain.schedule.entity.GroupSchedule;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
import jakarta.persistence.JoinColumn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupRoomDomain {
    private Long id;

    private String name;

    private String description;

    private UserDomain owner;

    private String groupCode; // 그룹방 고유 식별값

    private String thumbnail;

    private boolean alarm = true;

    private final Set<UserGroupDomain> userGroups = new HashSet<>();

    private final List<GroupSchedule> groupSchedules = new ArrayList<>();

    private final List<GroupFavoriteDomain> groupFavorites = new ArrayList<>();

}
