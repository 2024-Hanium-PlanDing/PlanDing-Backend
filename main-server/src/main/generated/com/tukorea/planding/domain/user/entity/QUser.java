package com.tukorea.planding.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1343008872L;

    public static final QUser user = new QUser("user");

    public final com.tukorea.planding.global.audit.QBaseEntity _super = new com.tukorea.planding.global.audit.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final ListPath<com.tukorea.planding.domain.group.entity.GroupFavorite, com.tukorea.planding.domain.group.entity.QGroupFavorite> groupFavorites = this.<com.tukorea.planding.domain.group.entity.GroupFavorite, com.tukorea.planding.domain.group.entity.QGroupFavorite>createList("groupFavorites", com.tukorea.planding.domain.group.entity.GroupFavorite.class, com.tukorea.planding.domain.group.entity.QGroupFavorite.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath profileImage = createString("profileImage");

    public final EnumPath<com.tukorea.planding.global.oauth.details.Role> role = createEnum("role", com.tukorea.planding.global.oauth.details.Role.class);

    public final StringPath socialId = createString("socialId");

    public final EnumPath<SocialType> socialType = createEnum("socialType", SocialType.class);

    public final StringPath userCode = createString("userCode");

    public final SetPath<com.tukorea.planding.domain.group.entity.UserGroup, com.tukorea.planding.domain.group.entity.QUserGroup> userGroup = this.<com.tukorea.planding.domain.group.entity.UserGroup, com.tukorea.planding.domain.group.entity.QUserGroup>createSet("userGroup", com.tukorea.planding.domain.group.entity.UserGroup.class, com.tukorea.planding.domain.group.entity.QUserGroup.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

