package com.tukorea.planding.global.valid.schedule;

import com.tukorea.planding.domain.schedule.dto.request.PersonalScheduleRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PersonalScheduleValidator implements ConstraintValidator<ValidScheduleTime, PersonalScheduleRequest> {
    @Override
    public boolean isValid(PersonalScheduleRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        return ScheduleTimeValidatorUtil.isValid(request.startTime(), request.endTime());
    }
}