package com.tukorea.planding.domain.schedule.entity.domain;

import com.tukorea.planding.domain.planner.entity.Planner;
import com.tukorea.planding.domain.schedule.entity.GroupSchedule;
import com.tukorea.planding.domain.schedule.entity.PersonalSchedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleType;
import com.tukorea.planding.global.audit.BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDomain {

        private Long id;

        private String title;

        private String content;

        private LocalDate scheduleDate;

        private Integer startTime;

        private Integer endTime;

        private boolean isComplete;

        private ScheduleType type;

        private PersonalScheduleDomain personalSchedule;

        private GroupScheduleDomain groupSchedule;

        private final List<Planner> planners = new ArrayList<>();

}
