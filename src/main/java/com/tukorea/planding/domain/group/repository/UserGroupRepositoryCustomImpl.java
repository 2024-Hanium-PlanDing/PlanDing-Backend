package com.tukorea.planding.domain.group.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tukorea.planding.domain.group.entity.UserGroup;
import com.tukorea.planding.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tukorea.planding.domain.group.entity.QUserGroup.userGroup;
import static com.tukorea.planding.domain.user.entity.QUser.user;


@Repository
@RequiredArgsConstructor
public class UserGroupRepositoryCustomImpl implements UserGroupRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByGroupRoomIdAndUserId(Long groupRoomId, Long userId) {
        return queryFactory.selectFrom(userGroup)
                .where(userGroup.groupRoom.id.eq(groupRoomId)
                        .and(userGroup.user.id.eq(userId)))
                .fetchCount() > 0;
    }

    @Override
    public List<User> findUserByIsConnectionFalse(Long groupRoomId) {
        return queryFactory.selectFrom(user)
                .join(user.userGroup, userGroup)
                .where(userGroup.groupRoom.id.eq(groupRoomId)
                        .and(userGroup.isConnected.eq(false)))
                .fetch();
    }

    public UserGroup findUserByGroupId(Long userId, Long groupRoomId) {
        return queryFactory.selectFrom(userGroup)
                .where(userGroup.groupRoom.id.eq(groupRoomId)
                        .and(userGroup.user.id.eq(userId)))
                .fetchFirst();
    }
}
