package com.tukorea.planding.domain.group.repository.normal;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tukorea.planding.domain.group.entity.QGroupRoom.groupRoom;
import static com.tukorea.planding.domain.group.entity.QUserGroup.userGroup;
import static com.tukorea.planding.domain.user.entity.QUser.user;


@Repository
@RequiredArgsConstructor
public class GroupRoomRepositoryCustomImpl implements GroupRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GroupRoom> findGroupRoomsByUserId(Long userId) {
        return queryFactory.select(groupRoom)
                .from(groupRoom)
                .join(groupRoom.userGroups, userGroup).fetchJoin()
                .where(groupRoom.userGroups.any().user.id.eq(userId))
                .fetch();
    }

    @Override
    public List<User> getGroupUsers(Long groupId) {
        return queryFactory.selectFrom(user)
                .innerJoin(user.userGroup, userGroup)
                .where(userGroup.groupRoom.id.eq(groupId))
                .fetch();
    }
}
