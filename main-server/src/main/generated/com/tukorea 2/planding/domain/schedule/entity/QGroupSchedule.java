package com.tukorea.planding.domain.schedule.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupSchedule is a Querydsl query type for GroupSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroupSchedule extends EntityPathBase<GroupSchedule> {

    private static final long serialVersionUID = -440754835L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroupSchedule groupSchedule = new QGroupSchedule("groupSchedule");

    public final com.tukorea.planding.domain.group.entity.QGroupRoom groupRoom;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SetPath<Schedule, QSchedule> schedules = this.<Schedule, QSchedule>createSet("schedules", Schedule.class, QSchedule.class, PathInits.DIRECT2);

    public QGroupSchedule(String variable) {
        this(GroupSchedule.class, forVariable(variable), INITS);
    }

    public QGroupSchedule(Path<? extends GroupSchedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGroupSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGroupSchedule(PathMetadata metadata, PathInits inits) {
        this(GroupSchedule.class, metadata, inits);
    }

    public QGroupSchedule(Class<? extends GroupSchedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.groupRoom = inits.isInitialized("groupRoom") ? new com.tukorea.planding.domain.group.entity.QGroupRoom(forProperty("groupRoom")) : null;
    }

}

