package com.tukorea.planding.domain.schedule.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPersonalSchedule is a Querydsl query type for PersonalSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPersonalSchedule extends EntityPathBase<PersonalSchedule> {

    private static final long serialVersionUID = 1908792928L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPersonalSchedule personalSchedule = new QPersonalSchedule("personalSchedule");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SetPath<Schedule, QSchedule> schedules = this.<Schedule, QSchedule>createSet("schedules", Schedule.class, QSchedule.class, PathInits.DIRECT2);

    public final com.tukorea.planding.domain.user.entity.QUser user;

    public QPersonalSchedule(String variable) {
        this(PersonalSchedule.class, forVariable(variable), INITS);
    }

    public QPersonalSchedule(Path<? extends PersonalSchedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPersonalSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPersonalSchedule(PathMetadata metadata, PathInits inits) {
        this(PersonalSchedule.class, metadata, inits);
    }

    public QPersonalSchedule(Class<? extends PersonalSchedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.tukorea.planding.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

