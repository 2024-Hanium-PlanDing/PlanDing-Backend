package com.tukorea.planding.domain.schedule.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupScheduleAttendance is a Querydsl query type for GroupScheduleAttendance
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroupScheduleAttendance extends EntityPathBase<GroupScheduleAttendance> {

    private static final long serialVersionUID = 337918006L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroupScheduleAttendance groupScheduleAttendance = new QGroupScheduleAttendance("groupScheduleAttendance");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QSchedule schedule;

    public final EnumPath<ScheduleStatus> status = createEnum("status", ScheduleStatus.class);

    public final com.tukorea.planding.domain.user.entity.QUser user;

    public QGroupScheduleAttendance(String variable) {
        this(GroupScheduleAttendance.class, forVariable(variable), INITS);
    }

    public QGroupScheduleAttendance(Path<? extends GroupScheduleAttendance> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGroupScheduleAttendance(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGroupScheduleAttendance(PathMetadata metadata, PathInits inits) {
        this(GroupScheduleAttendance.class, metadata, inits);
    }

    public QGroupScheduleAttendance(Class<? extends GroupScheduleAttendance> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.schedule = inits.isInitialized("schedule") ? new QSchedule(forProperty("schedule"), inits.get("schedule")) : null;
        this.user = inits.isInitialized("user") ? new com.tukorea.planding.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

