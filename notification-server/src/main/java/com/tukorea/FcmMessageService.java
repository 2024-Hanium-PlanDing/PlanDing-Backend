package com.tukorea;

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
public class FcmMessageService {

    private final UserService userService;

    public void sendNotification(NotificationDTO notificationDTO) {
        String fcmToken = userService.getFcmTokenByUserCode(notificationDTO.getUserCode());
        if (fcmToken != null) {
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .putData("message", notificationDTO.getMessage())
                    .putData("date", notificationDTO.getDate())
                    .putData("url", notificationDTO.getUrl())
                    .putData("userCode", notificationDTO.getUserCode())
                    .putData("type", String.valueOf(notificationDTO.getNotificationType()))
                    .putData("time", String.valueOf(notificationDTO.getTime()))
                    .build();

            try {
                FirebaseMessaging.getInstance().send(message);
                log.info("FCM 알림 전송 성공: " + message);
            } catch (Exception e) {
                log.error("FCM 알림 전송 실패: " + e.getMessage(), e);
            }
        } else {
            log.warn("FCM 토큰을 찾을 수 없습니다." + notificationDTO.getUserCode());
        }
    }

}
