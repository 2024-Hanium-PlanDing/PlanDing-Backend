package com.tukorea.planding.global.valid.schedule;

import com.tukorea.planding.domain.schedule.dto.request.GroupScheduleRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GroupScheduleValidator implements ConstraintValidator<ValidScheduleTime, GroupScheduleRequest> {
    @Override
    public boolean isValid(GroupScheduleRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        return ScheduleTimeValidatorUtil.isValid(request.startTime(), request.endTime());
    }
}
