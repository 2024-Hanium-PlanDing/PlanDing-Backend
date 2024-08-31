package com.tukorea.planding.domain.notify.service.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.tukorea.planding.domain.notify.dto.alarm.DailyNotificationDto;
import com.tukorea.planding.domain.notify.dto.alarm.NotificationDTO;
import com.tukorea.planding.domain.notify.entity.NotificationType;
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
                    .putData("title",notificationDTO.getTitle())
                    .putData("message", notificationDTO.getMessage())
                    .putData("date", notificationDTO.getDate())
                    .putData("url", notificationDTO.getUrl())
                    .putData("userCode", notificationDTO.getUserCode())
                    .putData("type", String.valueOf(notificationDTO.getNotificationType()))
                    .putData("time", String.valueOf(notificationDTO.getTime()))
                    .build();
            try {
                FirebaseMessaging.getInstance().send(message);
                log.info("[Personal Schedule] FCM 알림 전송 성공: " + message.toString());
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
                    .putData("title",notificationDTO.getTitle())
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
                log.info("[Group Schedule] FCM 알림 전송 성공: " + message.toString());
            } catch (Exception e) {
                log.error("[Group Schedule] FCM 알림 전송 실패: " + e.getMessage(), e);
            }
        } else {
            log.warn("[Group Schedule] FCM 토큰을 찾을 수 없습니다." + notificationDTO.getUserCode());
        }
    }

    public void dailyPublish(DailyNotificationDto dailyNotificationDto) {
        String fcmToken = userService.getFcmTokenByUserCode(dailyNotificationDto.userCode());
        if (fcmToken != null) {
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .putData("group", String.valueOf(dailyNotificationDto.group()))
                    .putData("personal", String.valueOf(dailyNotificationDto.personal()))
                    .putData("date", String.valueOf(dailyNotificationDto.date()))
                    .putData("userCode", dailyNotificationDto.userCode())
                    .putData("type", String.valueOf(NotificationType.DAILY))
                    .build();
            try {
                FirebaseMessaging.getInstance().send(message);
                log.info("[Daily] FCM 알림 전송 성공: " + message.toString());
            } catch (Exception e) {
                log.error("[Daily Schedule] FCM 알림 전송 실패: " + e.getMessage(), e);
            }
        } else {
            log.warn("[Daily Schedule] FCM 토큰을 찾을 수 없습니다." + dailyNotificationDto.userCode());
        }
    }

    public void notifyGroupScheduleCreation(String userCode, String groupName, String title, String url) {
        String fcmToken = userService.getFcmTokenByUserCode(userCode);
        if (fcmToken != null) {
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .putData("groupName", groupName)
                    .putData("title", title)
                    .putData("userCode", userCode)
                    .putData("type", String.valueOf(NotificationType.GROUP_SCHEDULE))
                    .putData("url", url)
                    .build();
            try {
                FirebaseMessaging.getInstance().send(message);
                log.info("[GROUP_SCHEDULE_CREATE] FCM 알림 전송 성공: userCode={}, title={}", userCode, title);
            } catch (Exception e) {
                log.error("[GROUP_SCHEDULE_CREATE] FCM 알림 전송 실패: userCode={}, error={}", userCode, e.getMessage(), e);
            }
        } else {
            log.warn("[GROUP_SCHEDULE_CREATE] FCM 토큰을 찾을 수 없습니다: userCode={}", userCode);
        }
    }
}
