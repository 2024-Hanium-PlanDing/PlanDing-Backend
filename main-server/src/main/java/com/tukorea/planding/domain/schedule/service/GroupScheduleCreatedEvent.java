package com.tukorea.planding.domain.schedule.service;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GroupScheduleCreatedEvent extends ApplicationEvent {
    private final String userCode;
    private final String groupName;
    private final String scheduleTitle;
    private final String url;

    public GroupScheduleCreatedEvent(Object source, String userCode, String groupName, String scheduleTitle, String url) {
        super(source);
        this.userCode = userCode;
        this.groupName = groupName;
        this.scheduleTitle = scheduleTitle;
        this.url = url;
    }
}
