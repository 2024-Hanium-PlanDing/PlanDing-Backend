package com.tukorea.planding.global.valid.schedule;

import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;

public class ScheduleTimeValidatorUtil {
    public static boolean isValid(Integer startTime, Integer endTime) {
        if (startTime == null || endTime == null) {
            throw new BusinessException(ErrorCode.INVALID_SCHEDULE_TIME);
        }

        if (startTime < 0 || startTime > 24 || endTime < 0 || endTime > 24) {
            return false;
        }

        if (startTime > endTime) {
            throw new BusinessException(ErrorCode.INVALID_SCHEDULE_TIME);
        }

        return true;
    }
}
