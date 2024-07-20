package com.tukorea.planding.domain.notify.service.schedule;

import com.tukorea.planding.domain.notify.entity.NotificationType;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationHandlerFactory {

    private final PersonalScheduleNotificationHandler personalScheduleNotificationHandler;
    private final GroupScheduleNotificationHandler groupScheduleNotificationHandler;

    public NotificationHandler getHandler(NotificationType notificationType) {
        switch (notificationType) {
            case PERSONAL_SCHEDULE:
                return personalScheduleNotificationHandler;
            case GROUP_SCHEDULE:
                return groupScheduleNotificationHandler;
            default:
                throw new BusinessException(ErrorCode.ALARM_NOT_FOUND);
        }
    }
}
