package com.tukorea.planding.domain.schedule.dto.response;

import com.tukorea.planding.domain.schedule.entity.ScheduleStatus;
import com.tukorea.planding.domain.schedule.entity.ScheduleType;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class GroupScheduleAttendanceResponse {
    private Long scheduleId;
    private String scheduleTitle;
    private String content;
    private LocalDate scheduleDate;
    private Integer startTime;
    private String groupName;
    private String groupCode;
    private String userCode;
    private String userName;
    private ScheduleStatus status;
    
}
