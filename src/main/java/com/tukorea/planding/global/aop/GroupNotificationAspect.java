package com.tukorea.planding.global.aop;

import com.tukorea.planding.domain.notify.service.schedule.GroupScheduleNotificationHandler;
import com.tukorea.planding.domain.notify.service.schedule.PersonalScheduleNotificationHandler;
import com.tukorea.planding.domain.schedule.dto.request.GroupScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.request.PersonalScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.response.PersonalScheduleResponse;
import com.tukorea.planding.domain.schedule.dto.response.ScheduleResponse;
import com.tukorea.planding.domain.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@RequiredArgsConstructor
public class GroupNotificationAspect {

    private final GroupScheduleNotificationHandler notificationHandler;

    @AfterReturning(pointcut = "execution(* com.tukorea.planding.domain.schedule.service.GroupScheduleService.createGroupSchedule(..)) && args(groupCode, groupScheduleRequest)", returning = "ScheduleResponse", argNames = "groupCode,groupScheduleRequest,response")
    public void registerNotification(String groupCode, GroupScheduleRequest groupScheduleRequest, ScheduleResponse response) {
        // 예약된 알림 등록 로직
        // 1시간전 등록
        notificationHandler.registerScheduleBeforeOneHour(groupScheduleRequest.userCode(), response);
    }

}
