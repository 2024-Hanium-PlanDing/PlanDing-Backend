package com.tukorea.planding.domain.notify.service.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.tukorea.planding.domain.notify.dto.NotificationDTO;
import com.tukorea.planding.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FCMService {

    private final UserService userService;

    public void personalPublish(NotificationDTO notificationDTO) {
        String fcmToken = userService.getFcmTokenByUserCode(notificationDTO.getUserCode());
        if (fcmToken != null) {
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .putData("scheduleId", String.valueOf(notificationDTO.getScheduleId()))
                    .putData("message", notificationDTO.getMessage())
                    .putData("date", notificationDTO.getDate())
                    .putData("url", notificationDTO.getUrl())
                    .putData("userCode", notificationDTO.getUserCode())
                    .putData("type", String.valueOf(notificationDTO.getNotificationType()))
                    .putData("time", String.valueOf(notificationDTO.getTime()))
                    .build();

            try {
                FirebaseMessaging.getInstance().send(message);
                log.info("[Personal Schedule] FCM 알림 전송 성공: " + message);
            } catch (Exception e) {
                log.error("[Personal Schedule] FCM 알림 전송 실패: " + e.getMessage(), e);
            }
        } else {
            log.warn("[Personal Schedule] FCM 토큰을 찾을 수 없습니다." + notificationDTO.getUserCode());
        }
    }

    public void groupPublish(NotificationDTO notificationDTO) {
        String fcmToken = userService.getFcmTokenByUserCode(notificationDTO.getUserCode());
        if (fcmToken != null) {
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .putData("scheduleId", String.valueOf(notificationDTO.getScheduleId()))
                    .putData("message", notificationDTO.getMessage())
                    .putData("date", notificationDTO.getDate())
                    .putData("url", notificationDTO.getUrl())
                    .putData("userCode", notificationDTO.getUserCode())
                    .putData("type", String.valueOf(notificationDTO.getNotificationType()))
                    .putData("time", String.valueOf(notificationDTO.getTime()))
                    .putData("groupName", notificationDTO.getGroupName())
                    .putData("groupCode", notificationDTO.getGroupCode())
                    .build();

            try {
                FirebaseMessaging.getInstance().send(message);
                log.info("[Group Schedule] FCM 알림 전송 성공: " + message);
            } catch (Exception e) {
                log.error("[Group Schedule] FCM 알림 전송 실패: " + e.getMessage(), e);
            }
        } else {
            log.warn("[Group Schedule] FCM 토큰을 찾을 수 없습니다." + notificationDTO.getUserCode());
        }
    }
}
