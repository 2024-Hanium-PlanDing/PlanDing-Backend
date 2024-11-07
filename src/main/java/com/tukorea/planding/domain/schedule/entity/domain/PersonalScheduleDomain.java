package com.tukorea.planding.domain.schedule.entity.domain;

import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.user.entity.User;

import java.util.HashSet;
import java.util.Set;

public class PersonalScheduleDomain {

        private Long id;

        private User user;

        private Set<ScheduleDomain> schedules = new HashSet<>();

}
