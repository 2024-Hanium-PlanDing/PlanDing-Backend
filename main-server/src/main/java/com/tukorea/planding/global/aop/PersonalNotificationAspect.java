package com.tukorea.planding.global.aop;

import com.tukorea.planding.domain.notify.service.NotificationHandler;
import com.tukorea.planding.domain.notify.service.schedule.PersonalScheduleNotificationHandler;
import com.tukorea.planding.domain.schedule.dto.request.PersonalScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.response.PersonalScheduleResponse;
import com.tukorea.planding.domain.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class PersonalNotificationAspect {

    private final PersonalScheduleNotificationHandler notificationHandler;

    /**
     * 스케줄 생성시 알람 등록
     *
     * @param userInfo
     * @param personalScheduleRequest
     * @param personalScheduleResponse
     */
    @AfterReturning(pointcut = "execution(* com.tukorea.planding.domain.schedule.service.PersonalScheduleService.createSchedule(..)) && args(userInfo, personalScheduleRequest)", returning = "personalScheduleResponse", argNames = "userInfo,personalScheduleRequest,personalScheduleResponse")
    public void registerNotification(UserInfo userInfo, PersonalScheduleRequest personalScheduleRequest, PersonalScheduleResponse personalScheduleResponse) {
        // 예약된 알림 등록 로직
        // 1시간전 등록
        notificationHandler.registerScheduleBeforeOneHour(userInfo.getUserCode(), personalScheduleResponse);
        notificationHandler.registerScheduleBeforeDay(userInfo.getUserCode(), personalScheduleResponse);
    }

    /**
     * 스케줄 수정시 알람 수정
     *
     * @param scheduleId
     * @param personalScheduleRequest
     * @param userInfo
     * @param personalScheduleResponse
     */
    @AfterReturning(pointcut = "execution(* com.tukorea.planding.domain.schedule.service.PersonalScheduleService.updateSchedule(..)) && args(scheduleId, personalScheduleRequest, userInfo)", returning = "personalScheduleResponse", argNames = "scheduleId,personalScheduleRequest,userInfo,personalScheduleResponse")
    public void updateNotification(Long scheduleId, PersonalScheduleRequest personalScheduleRequest, UserInfo userInfo, PersonalScheduleResponse personalScheduleResponse) {
        // 예약된 알림 등록 로직을 수정
        notificationHandler.updateScheduleBeforeOneHour(scheduleId, personalScheduleResponse, userInfo);
    }

    /**
     * 스케줄 삭제시 알람 삭제
     *
     * @param scheduleId
     * @param userInfo
     */
    @AfterReturning(pointcut = "execution(* com.tukorea.planding.domain.schedule.service.PersonalScheduleService.deleteSchedule(..)) && args(userInfo, scheduleId)", argNames = "scheduleId,userInfo")
    public void deleteNotification(Long scheduleId, UserInfo userInfo) {
        // 예약된 알림 삭제
        notificationHandler.deleteScheduleBeforeOneHour(scheduleId);
    }

}
