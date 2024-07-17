package com.tukorea.planding.domain.schedule.service;

import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.group.service.query.UserGroupQueryService;
import com.tukorea.planding.domain.schedule.dto.request.GroupScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.request.websocket.SendCreateScheduleDTO;
import com.tukorea.planding.domain.schedule.dto.request.websocket.SendDeleteScheduleDTO;
import com.tukorea.planding.domain.schedule.dto.request.websocket.SendUpdateScheduleDTO;
import com.tukorea.planding.domain.schedule.dto.response.GroupScheduleResponse;
import com.tukorea.planding.domain.schedule.dto.response.ScheduleResponse;
import com.tukorea.planding.domain.schedule.dto.response.UserScheduleAttendance;
import com.tukorea.planding.domain.schedule.dto.response.websocket.SendGroupResponse;
import com.tukorea.planding.domain.schedule.entity.*;
import com.tukorea.planding.domain.schedule.repository.GroupScheduleAttendanceRepository;
import com.tukorea.planding.domain.schedule.repository.GroupScheduleRepository;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class GroupScheduleService {

    private final ScheduleQueryService scheduleQueryService;
    private final GroupQueryService groupQueryService;
    private final UserGroupQueryService userGroupQueryService;

    private final GroupScheduleRepository groupScheduleRepository;
    private final GroupScheduleAttendanceRepository groupScheduleAttendanceRepository;
    private final ApplicationEventPublisher eventPublisher;


    public SendGroupResponse createGroupSchedule(String groupCode, SendCreateScheduleDTO request) {
        log.info("Create 그룹스케줄 groupCode: {}, request: {}", groupCode, request);
        checkUserAccessToGroupRoom(groupCode, request.userCode());
        checkRequestGroupRoom(groupCode, request.groupCode());

        GroupRoom groupRoom = groupQueryService.getGroupByCode(groupCode);

        checkUserAccessToGroupRoom(request.groupCode(), request.userCode());

        GroupSchedule groupSchedule = GroupSchedule.builder()
                .groupRoom(groupRoom)
                .build();

        Schedule newSchedule = Schedule.builder()
                .title(request.title())
                .content(request.content())
                .scheduleDate(request.scheduleDate())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .isComplete(false)
                .groupSchedule(groupSchedule)
                .type(ScheduleType.GROUP)
                .build();

        groupSchedule.getSchedules().add(newSchedule);
        groupScheduleRepository.save(groupSchedule);
        Schedule savedSchedule = scheduleQueryService.save(newSchedule);

        notifyUsers(groupRoom, savedSchedule);
        return SendGroupResponse.from(savedSchedule, Action.CREATE);
    }

    @Transactional(readOnly = true)
    public GroupScheduleResponse getGroupScheduleById(UserInfo userInfo, String groupCode, Long scheduleId) {
        checkUserAccessToGroupRoom(groupCode, userInfo.getUserCode());

        GroupRoom groupRoom = groupQueryService.getGroupByCode(groupCode);
        Schedule schedule = scheduleQueryService.findScheduleById(scheduleId);

        List<UserScheduleAttendance> attendances = getUserScheduleAttendances(groupRoom, scheduleId);

        return GroupScheduleResponse.from(schedule, groupRoom.getName(), attendances);
    }

    @Transactional(readOnly = true)
    public List<GroupScheduleResponse> getSchedulesByGroupRoom(String groupCode, UserInfo userInfo) {
        checkUserAccessToGroupRoom(groupCode, userInfo.getUserCode());
        List<Schedule> schedules = scheduleQueryService.findByGroupRoomCode(groupCode);
        GroupRoom groupRoom = groupQueryService.getGroupByCode(groupCode);

        return schedules.stream()
                .map(schedule -> GroupScheduleResponse.from(schedule, groupRoom.getName(), getUserScheduleAttendances(groupRoom, schedule.getId())))
                .collect(Collectors.toList());
    }

    public SendGroupResponse updateScheduleByGroupRoom(String groupCode, SendUpdateScheduleDTO request) {
        log.info("Updated 그룹스케줄 groupCode: {}, request: {}", groupCode, request);
        checkUserAccessToGroupRoom(groupCode, request.userCode());
        checkRequestGroupRoom(groupCode, request.groupCode());

        Schedule schedule = scheduleQueryService.findScheduleById(request.scheduleId());
        schedule.update(request.title(), request.content(), request.startTime(), request.endTime());

        return SendGroupResponse.from(schedule, Action.UPDATE);
    }

    public SendGroupResponse deleteScheduleByGroupRoom(String groupCode, SendDeleteScheduleDTO request) {
        log.info("DELETE 그룹스케줄 groupCode: {}, request: {}", groupCode, request);
        checkUserAccessToGroupRoom(groupCode, request.userCode());
        checkRequestGroupRoom(groupCode, request.groupCode());
        scheduleQueryService.deleteById(request.scheduleId());
        return SendGroupResponse.delete(request.scheduleId(), Action.DELETE);
    }

    public List<ScheduleResponse> getWeekSchedule(LocalDate startDate, LocalDate endDate, UserInfo userInfo) {
        return scheduleQueryService.findWeeklyGroupScheduleByUser(startDate, endDate, userInfo.getId())
                .stream()
                .map(ScheduleResponse::from)
                .sorted(ScheduleResponse.getComparatorByStartTime())
                .collect(Collectors.toList());
    }

    public List<ScheduleResponse> getWeekScheduleByGroupCode(LocalDate startDate, LocalDate endDate, String groupRoomCode, UserInfo userInfo) {
        return scheduleQueryService.findWeeklyGroupScheduleByGroupCode(startDate, endDate, userInfo.getId(), groupRoomCode)
                .stream()
                .map(ScheduleResponse::from)
                .sorted(ScheduleResponse.getComparatorByStartTime())
                .collect(Collectors.toList());
    }


    // 유저가 그룹룸에 접근할 권리가있는지 확인
    private void checkUserAccessToGroupRoom(String groupCode, String userCode) {
        if (!userGroupQueryService.checkUserAccessToGroupRoom(groupCode, userCode)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
    }

    // 요청한 그룹코드와 접속한 그룹코드가 같은지
    private void checkRequestGroupRoom(String groupCode, String compareGroupCode) {
        if (!groupCode.equals(compareGroupCode)) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
    }


    private List<UserScheduleAttendance> getUserScheduleAttendances(GroupRoom groupRoom, Long scheduleId) {
        return groupRoom.getUserGroups().stream()
                .map(userGroup -> {
                    Optional<GroupScheduleAttendance> attendance = groupScheduleAttendanceRepository.findByUserIdAndScheduleIdAndStatusNot(userGroup.getUser().getId(), scheduleId, ScheduleStatus.UNDECIDED);
                    ScheduleStatus status = attendance.map(GroupScheduleAttendance::getStatus).orElse(ScheduleStatus.UNDECIDED);
                    return new UserScheduleAttendance(userGroup.getUser().getUserCode(), userGroup.getUser().getUsername(), status);
                })
                .filter(attendance -> attendance.status() != ScheduleStatus.UNDECIDED)
                .collect(Collectors.toList());
    }

    private void notifyUsers(GroupRoom groupRoom, Schedule savedSchedule) {
        List<User> notificationUsers = userGroupQueryService.findUserByIsConnectionFalse(groupRoom.getId());
        notificationUsers.forEach(member -> {
            GroupScheduleCreatedEvent event = new GroupScheduleCreatedEvent(this,
                    member.getUserCode(),
                    groupRoom.getName(),
                    savedSchedule.getTitle(),
                    "/groupRoom/" + groupRoom.getId() + "/" + savedSchedule.getId());
            eventPublisher.publishEvent(event);
        });
    }
}
