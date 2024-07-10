package com.tukorea.planding.global.valid.schedule;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {PersonalScheduleValidator.class, GroupScheduleValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidScheduleTime {
    String message() default "Invalid schedule time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}