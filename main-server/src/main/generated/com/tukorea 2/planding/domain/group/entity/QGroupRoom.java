package com.tukorea.planding.domain.group.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupRoom is a Querydsl query type for GroupRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroupRoom extends EntityPathBase<GroupRoom> {

    private static final long serialVersionUID = -356420013L;

    public static final QGroupRoom groupRoom = new QGroupRoom("groupRoom");

    public final com.tukorea.planding.global.audit.QBaseEntity _super = new com.tukorea.planding.global.audit.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath description = createString("description");

    public final StringPath groupCode = createString("groupCode");

    public final ListPath<GroupFavorite, QGroupFavorite> groupFavorites = this.<GroupFavorite, QGroupFavorite>createList("groupFavorites", GroupFavorite.class, QGroupFavorite.class, PathInits.DIRECT2);

    public final ListPath<com.tukorea.planding.domain.schedule.entity.GroupSchedule, com.tukorea.planding.domain.schedule.entity.QGroupSchedule> groupSchedules = this.<com.tukorea.planding.domain.schedule.entity.GroupSchedule, com.tukorea.planding.domain.schedule.entity.QGroupSchedule>createList("groupSchedules", com.tukorea.planding.domain.schedule.entity.GroupSchedule.class, com.tukorea.planding.domain.schedule.entity.QGroupSchedule.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath name = createString("name");

    public final StringPath owner = createString("owner");

    public final StringPath thumbnail = createString("thumbnail");

    public final SetPath<UserGroup, QUserGroup> userGroups = this.<UserGroup, QUserGroup>createSet("userGroups", UserGroup.class, QUserGroup.class, PathInits.DIRECT2);

    public QGroupRoom(String variable) {
        super(GroupRoom.class, forVariable(variable));
    }

    public QGroupRoom(Path<? extends GroupRoom> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGroupRoom(PathMetadata metadata) {
        super(GroupRoom.class, metadata);
    }

}

