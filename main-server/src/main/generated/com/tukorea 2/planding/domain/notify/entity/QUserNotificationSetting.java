package com.tukorea.planding.domain.notify.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserNotificationSetting is a Querydsl query type for UserNotificationSetting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserNotificationSetting extends EntityPathBase<UserNotificationSetting> {

    private static final long serialVersionUID = -654716065L;

    public static final QUserNotificationSetting userNotificationSetting = new QUserNotificationSetting("userNotificationSetting");

    public final BooleanPath groupScheduleNotificationEnabled = createBoolean("groupScheduleNotificationEnabled");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath scheduleNotificationEnabled = createBoolean("scheduleNotificationEnabled");

    public final StringPath userCode = createString("userCode");

    public QUserNotificationSetting(String variable) {
        super(UserNotificationSetting.class, forVariable(variable));
    }

    public QUserNotificationSetting(Path<? extends UserNotificationSetting> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserNotificationSetting(PathMetadata metadata) {
        super(UserNotificationSetting.class, metadata);
    }

}

