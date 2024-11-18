package com.tukorea.planding.domain.schedule.service;

import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.schedule.dto.request.GroupScheduleAttendanceRequest;
import com.tukorea.planding.domain.schedule.dto.response.GroupScheduleAttendanceResponse;
import com.tukorea.planding.domain.schedule.dto.response.UserScheduleAttendance;
import com.tukorea.planding.domain.schedule.entity.GroupScheduleAttendance;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleStatus;
import com.tukorea.planding.domain.schedule.repository.GroupScheduleAttendanceRepository;
import com.tukorea.planding.domain.user.dto.UserResponse;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.entity.UserDomain;
import com.tukorea.planding.domain.user.service.UserQueryService;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupScheduleAttendanceService {

    private final UserQueryService userQueryService;
    private final ScheduleQueryService scheduleQueryService;
    private final GroupScheduleAttendanceRepository groupScheduleAttendanceRepository;

    @Transactional
    public GroupScheduleAttendanceResponse participationGroupSchedule(UserResponse userResponse, GroupScheduleAttendanceRequest request) {
        UserDomain user = userQueryService.getUserByUserCode(userResponse.getUserCode());
        Schedule schedule = scheduleQueryService.findScheduleById(request.scheduleId());

        GroupScheduleAttendance attendance = groupScheduleAttendanceRepository
                .findByUserIdAndScheduleId(user.getId(), schedule.getId())
                .orElseGet(GroupScheduleAttendance::new);

        attendance.addUser(User.fromModel(user));
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

        return GroupScheduleAttendanceResponse.builder()
                .scheduleId(schedule.getId())
                .scheduleTitle(schedule.getTitle())
                .startTime(schedule.getStartTime())
                .scheduleDate(schedule.getScheduleDate())
                .content(schedule.getContent())
                .groupCode(schedule.getGroupSchedule().getGroupRoom().getGroupCode())
                .groupName(schedule.getGroupSchedule().getGroupRoom().getName())
                .userName(user.getUsername())
                .userCode(user.getUserCode())
                .status(attendance.getStatus())
                .build();
    }

    @Transactional(readOnly = true)
    public List<UserScheduleAttendance> getUserScheduleAttendances(GroupRoom groupRoom, Long scheduleId) {
        return groupRoom.getUserGroups().stream()
                .map(userGroup -> {
                    Optional<GroupScheduleAttendance> attendance = groupScheduleAttendanceRepository.findByUserIdAndScheduleIdAndStatusNot(userGroup.getUser().getId(), scheduleId, ScheduleStatus.UNDECIDED);
                    ScheduleStatus status = attendance.map(GroupScheduleAttendance::getStatus).orElse(ScheduleStatus.UNDECIDED);
                    return new UserScheduleAttendance(userGroup.getUser().getUserCode(), userGroup.getUser().getUsername(), status);
                })
                .filter(attendance -> attendance.status() != ScheduleStatus.UNDECIDED)
                .collect(Collectors.toList());
    }

}
