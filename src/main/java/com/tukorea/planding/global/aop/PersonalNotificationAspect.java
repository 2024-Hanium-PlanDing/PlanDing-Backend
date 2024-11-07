package com.tukorea.planding.global.aop;

import com.tukorea.planding.domain.notify.service.schedule.PersonalScheduleNotificationHandler;
import com.tukorea.planding.domain.schedule.dto.request.PersonalScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.response.PersonalScheduleResponse;
import com.tukorea.planding.domain.user.dto.UserResponse;
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
     * @param userResponse
     * @param personalScheduleRequest
     * @param personalScheduleResponse
     */
    @AfterReturning(pointcut = "execution(* com.tukorea.planding.domain.schedule.service.PersonalScheduleService.createSchedule(..)) && args(userResponse, personalScheduleRequest)", returning = "personalScheduleResponse", argNames = "userResponse,personalScheduleRequest,personalScheduleResponse")
    public void registerNotification(UserResponse userResponse, PersonalScheduleRequest personalScheduleRequest, PersonalScheduleResponse personalScheduleResponse) {
        // 예약된 알림 등록 로직
        // 1시간전 등록
        notificationHandler.registerScheduleBeforeOneHour(userResponse.getUserCode(), personalScheduleResponse);
        notificationHandler.registerScheduleBeforeDay(userResponse.getUserCode(), personalScheduleResponse);
    }

    /**
     * 스케줄 수정시 알람 수정
     *
     * @param scheduleId
     * @param personalScheduleRequest
     * @param userResponse
     * @param personalScheduleResponse
     */
    @AfterReturning(pointcut = "execution(* com.tukorea.planding.domain.schedule.service.PersonalScheduleService.updateSchedule(..)) && args(scheduleId, personalScheduleRequest, userResponse)", returning = "personalScheduleResponse", argNames = "scheduleId,personalScheduleRequest,userResponse,personalScheduleResponse")
    public void updateNotification(Long scheduleId, PersonalScheduleRequest personalScheduleRequest, UserResponse userResponse, PersonalScheduleResponse personalScheduleResponse) {
        // 예약된 알림 등록 로직을 수정
        notificationHandler.updateScheduleBeforeOneHour(scheduleId, personalScheduleResponse, userResponse);
    }

    /**
     * 스케줄 삭제시 알람 삭제
     *
     * @param scheduleId
     * @param userResponse
     */
    @AfterReturning(pointcut = "execution(* com.tukorea.planding.domain.schedule.service.PersonalScheduleService.deleteSchedule(..)) && args(userResponse, scheduleId)", argNames = "scheduleId,userResponse")
    public void deleteNotification(Long scheduleId, UserResponse userResponse) {
        // 예약된 알림 삭제
        notificationHandler.deleteScheduleBeforeOneHour(scheduleId);
    }

}
