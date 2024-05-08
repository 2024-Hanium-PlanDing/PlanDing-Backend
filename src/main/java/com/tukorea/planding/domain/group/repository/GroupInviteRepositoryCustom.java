package com.tukorea.planding.domain.group.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tukorea.planding.domain.group.entity.GroupInvite;
import com.tukorea.planding.domain.group.entity.InviteStatus;
import com.tukorea.planding.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tukorea.planding.domain.group.entity.QGroupInvite.groupInvite;


@Repository
@RequiredArgsConstructor
public class GroupInviteRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public List<GroupInvite> findPendingInvitationsForUser(Long userId) {
        return queryFactory.selectFrom(groupInvite)
                .where(groupInvite.invitedUser.id.eq(userId)
                        .and(groupInvite.inviteStatus.eq(InviteStatus.PENDING)))
                .fetch();
    }

    public Long countByInvitedUserCodeAndStatus(String userCode, InviteStatus inviteStatus) {
        return queryFactory.select(groupInvite.count())
                .from(groupInvite)
                .where(groupInvite.invitedUser.userCode.eq(userCode).and(
                        groupInvite.inviteStatus.eq(inviteStatus)
                ))
                .fetchOne();
    }
}
