package com.tukorea.planding.global.valid.schedule;

import com.tukorea.planding.domain.schedule.dto.request.GroupScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.request.websocket.SendCreateScheduleDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GroupScheduleValidator implements ConstraintValidator<ValidScheduleTime, SendCreateScheduleDTO> {
    @Override
    public boolean isValid(SendCreateScheduleDTO request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        return ScheduleTimeValidatorUtil.isValid(request.startTime(), request.endTime());
    }
}
