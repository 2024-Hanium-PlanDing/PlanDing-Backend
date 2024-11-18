package com.tukorea.planding.global.aop;

import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.group.service.query.UserGroupQueryService;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class GroupAccessAspect {

    private final UserGroupQueryService userGroupQueryService;
    @Before("execution(* com.tukorea.planding.domain.schedule.service.GroupScheduleService.*(..)) && args(userCode, groupCode, ..)")
    public void checkUserAccessToGroupRoom(String userCode, String groupCode) {
        if (!userGroupQueryService.checkUserAccessToGroupRoom(groupCode, userCode)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
    }
}
