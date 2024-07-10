package com.tukorea.planding.domain.schedule.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSchedule is a Querydsl query type for Schedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSchedule extends EntityPathBase<Schedule> {

    private static final long serialVersionUID = -1812722432L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSchedule schedule = new QSchedule("schedule");

    public final com.tukorea.planding.global.audit.QBaseEntity _super = new com.tukorea.planding.global.audit.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Integer> endTime = createNumber("endTime", Integer.class);

    public final QGroupSchedule groupSchedule;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isComplete = createBoolean("isComplete");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final QPersonalSchedule personalSchedule;

    public final DatePath<java.time.LocalDate> scheduleDate = createDate("scheduleDate", java.time.LocalDate.class);

    public final NumberPath<Integer> startTime = createNumber("startTime", Integer.class);

    public final StringPath title = createString("title");

    public final EnumPath<ScheduleType> type = createEnum("type", ScheduleType.class);

    public QSchedule(String variable) {
        this(Schedule.class, forVariable(variable), INITS);
    }

    public QSchedule(Path<? extends Schedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSchedule(PathMetadata metadata, PathInits inits) {
        this(Schedule.class, metadata, inits);
    }

    public QSchedule(Class<? extends Schedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.groupSchedule = inits.isInitialized("groupSchedule") ? new QGroupSchedule(forProperty("groupSchedule"), inits.get("groupSchedule")) : null;
        this.personalSchedule = inits.isInitialized("personalSchedule") ? new QPersonalSchedule(forProperty("personalSchedule"), inits.get("personalSchedule")) : null;
    }

}

