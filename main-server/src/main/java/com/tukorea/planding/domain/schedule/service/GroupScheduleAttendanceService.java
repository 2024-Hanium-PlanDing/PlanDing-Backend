package com.tukorea.planding.domain.schedule.service;

import com.tukorea.planding.domain.schedule.dto.request.GroupScheduleAttendanceRequest;
import com.tukorea.planding.domain.schedule.entity.GroupScheduleAttendance;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.repository.GroupScheduleAttendanceRepository;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.service.UserQueryService;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupScheduleAttendanceService {

    private final GroupScheduleAttendanceRepository groupScheduleAttendanceRepository;
    private final UserQueryService userQueryService;
    private final ScheduleQueryService scheduleQueryService;

    @Transactional
    public void participationGroupSchedule(UserInfo userInfo, GroupScheduleAttendanceRequest request) {
        User user = userQueryService.getUserByUserCode(userInfo.getUserCode());
        Schedule schedule = scheduleQueryService.findScheduleById(request.scheduleId());

        GroupScheduleAttendance attendance = groupScheduleAttendanceRepository
                .findByUserIdAndScheduleId(user.getId(), schedule.getId())
                .orElseGet(GroupScheduleAttendance::new);

        attendance.addUser(user);
        attendance.addSchedule(schedule);

        switch (request.status()) {
            case POSSIBLE:
                attendance.markAsPossible();
                break;
            case IMPOSSIBLE:
                attendance.markAsImpossible();
                break;
            case UNDECIDED:
                attendance.markAsUndecided();
                break;
            default:
                throw new BusinessException(ErrorCode.INVALID_ATTENDANCE_STATUS);
        }

        groupScheduleAttendanceRepository.save(attendance);
    }
}
