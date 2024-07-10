package com.tukorea.planding.domain.group.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupFavorite is a Querydsl query type for GroupFavorite
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroupFavorite extends EntityPathBase<GroupFavorite> {

    private static final long serialVersionUID = 1858487060L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroupFavorite groupFavorite = new QGroupFavorite("groupFavorite");

    public final QGroupRoom groupRoom;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.tukorea.planding.domain.user.entity.QUser user;

    public QGroupFavorite(String variable) {
        this(GroupFavorite.class, forVariable(variable), INITS);
    }

    public QGroupFavorite(Path<? extends GroupFavorite> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGroupFavorite(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGroupFavorite(PathMetadata metadata, PathInits inits) {
        this(GroupFavorite.class, metadata, inits);
    }

    public QGroupFavorite(Class<? extends GroupFavorite> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.groupRoom = inits.isInitialized("groupRoom") ? new QGroupRoom(forProperty("groupRoom")) : null;
        this.user = inits.isInitialized("user") ? new com.tukorea.planding.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

