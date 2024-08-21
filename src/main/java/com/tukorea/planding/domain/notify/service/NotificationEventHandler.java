package com.tukorea.planding.domain.notify.service;

import com.tukorea.planding.domain.group.dto.GroupInviteEvent;
import com.tukorea.planding.domain.notify.dto.alarm.NotificationDTO;
import com.tukorea.planding.domain.notify.entity.NotificationType;
import com.tukorea.planding.domain.schedule.service.GroupScheduleCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

    private final ApplicationEventPublisher eventPublisher;
    private final RedisMessageService redisMessageService;

    //TODO: Builder패턴 NotificationDTO정의
    @EventListener
    public void handleGroupInvitedEvent(GroupInviteEvent event) {
        NotificationDTO request = NotificationDTO.builder()
                .notificationType(NotificationType.INVITE)
                .groupName(event.getGroupName())
                .message(event.getGroupName() + "그룹으로 부터 초대되었습니다.")
                .userCode(event.getUserCode())
                .build();

        // 구독 발행
        redisMessageService.publish(event.getUserCode(), request);
    }

    @EventListener
    public void handleGroupScheduleCreatedEvent(GroupScheduleCreatedEvent event) {
        NotificationDTO request = NotificationDTO.builder()
                .notificationType(NotificationType.GROUP_SCHEDULE)
                .groupName(event.getGroupName())
                .message("새로운 그룹 스케줄이 생성되었습니다: " + event.getScheduleTitle())
                .userCode(event.getUserCode())
                .url(event.getUrl())
                .build();

        redisMessageService.publish(event.getUserCode(), request);
    }

    // 그룹 초대 알림 코드
    public void notifyInvitation(final String userCode, final String groupName) {
        GroupInviteEvent event = new GroupInviteEvent(this, userCode, groupName);
        eventPublisher.publishEvent(event);
    }
}
