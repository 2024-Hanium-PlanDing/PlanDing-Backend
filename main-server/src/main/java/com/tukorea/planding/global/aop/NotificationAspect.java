package com.tukorea.planding.global.aop;

import com.tukorea.planding.domain.notify.service.NotificationHandler;
import com.tukorea.planding.domain.schedule.dto.request.PersonalScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.response.PersonalScheduleResponse;
import com.tukorea.planding.domain.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class NotificationAspect {

    private final NotificationHandler notificationHandler;

    @AfterReturning(pointcut = "execution(* com.tukorea.planding.domain.schedule.service.PersonalScheduleService.createSchedule(..)) && args(userInfo, personalScheduleRequest)", returning = "personalScheduleResponse")
    public void registerNotification(UserInfo userInfo, PersonalScheduleRequest personalScheduleRequest, PersonalScheduleResponse personalScheduleResponse) {
        // 예약된 알림 등록 로직
        // 1시간전 등록
        notificationHandler.registerScheduleBeforeOneHour(userInfo.getUserCode(), personalScheduleResponse);
        notificationHandler.registerScheduleBeforeDay(userInfo.getUserCode(), personalScheduleResponse);
    }

//    @AfterThrowing(pointcut = "execution(* com.example.ScheduleService.createSchedule(..))", throwing = "ex")
//    public void handleScheduleCreationException(Exception ex) {
//        // 스케줄 생성 중 예외 발생 시 처리
//        // 여기서 추가적인 롤백이 필요하다면 처리 가능
//    }
}
