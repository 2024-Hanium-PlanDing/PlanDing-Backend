package com.tukorea.planding.domain.notify.service;

import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.service.query.UserGroupQueryService;
import com.tukorea.planding.domain.notify.service.fcm.FCMService;
import com.tukorea.planding.domain.notify.service.schedule.GroupScheduleNotificationHandler;
import com.tukorea.planding.domain.schedule.entity.Action;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.service.GroupScheduleCreatedEvent;
import com.tukorea.planding.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupScheduleNotificationService {

    private final FCMService fcmService;
    private final ApplicationEventPublisher eventPublisher;
    private final GroupScheduleNotificationHandler notificationHandler;
    private final UserGroupQueryService userGroupQueryService;

    public void registerGroupScheduleNotification(String userCode, GroupRoom groupRoom, Schedule schedule, Action action) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                if (action == Action.CREATE) {
                    notificationHandler.registerScheduleBeforeOneHour(userCode, schedule);
                    notifyGroupScheduleCreation(groupRoom, schedule);
                } else if (action == Action.UPDATE) {
                    notificationHandler.updateScheduleBeforeOneHour(userCode, schedule);
                } else if (action == Action.DELETE) {
                    notificationHandler.deleteScheduleBeforeOneHour(schedule.getId());
                }
            }
        });
    }

    private void notifyGroupScheduleCreation(GroupRoom groupRoom, Schedule schedule) {
        List<User> notificationUsers = userGroupQueryService.findUserByIsConnectionFalse(groupRoom.getId());
        notificationUsers.forEach(member -> {
            String userCode = member.getUserCode();
            String groupName = groupRoom.getName();
            String scheduleTitle = schedule.getTitle();
            String url = "/groupRoom/" + groupRoom.getGroupCode() + "/" + schedule.getId();

            // FCM 알림 전송
            fcmService.notifyGroupScheduleCreation(userCode, groupName, scheduleTitle, url);

            // 이벤트 발행
            GroupScheduleCreatedEvent event = new GroupScheduleCreatedEvent(
                    this, userCode, groupName, scheduleTitle, url);
            eventPublisher.publishEvent(event);
        });

    }

}
