package com.tukorea.planding.domain.group.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GroupInviteEvent extends ApplicationEvent {

    private final String userCode;
    private final String groupName;

    public GroupInviteEvent(Object source, String userCode, String groupName) {
        super(source);
        this.userCode = userCode;
        this.groupName = groupName;
    }

}
