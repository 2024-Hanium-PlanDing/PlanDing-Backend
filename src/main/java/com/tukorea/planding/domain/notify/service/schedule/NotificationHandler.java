package com.tukorea.planding.domain.notify.service.schedule;

import com.tukorea.planding.domain.notify.dto.NotificationDTO;

public interface NotificationHandler {
    public void sendNotification(NotificationDTO request);
}
