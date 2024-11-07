package com.tukorea.planding.domain.schedule.service;

import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.group.service.query.UserGroupQueryService;
import com.tukorea.planding.domain.notify.service.fcm.FCMService;
import com.tukorea.planding.domain.notify.service.schedule.GroupScheduleNotificationHandler;
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
import com.tukorea.planding.domain.user.dto.UserResponse;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
    private final FCMService fcmService;

    private final GroupScheduleRepository groupScheduleRepository;
    private final GroupScheduleAttendanceRepository groupScheduleAttendanceRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final GroupScheduleNotificationHandler groupScheduleNotificationHandler;


    public SendGroupResponse createGroupSchedule(String userCode, String groupCode, SendCreateScheduleDTO request) {
        log.info("Create 그룹스케줄 groupCode: {}, request: {}", groupCode, request);
        checkUserAccessToGroupRoom(groupCode, userCode);

        GroupRoom groupRoom = groupQueryService.getGroupByCode(groupCode);

        checkUserAccessToGroupRoom(groupCode, userCode);

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

        /* 커밋 이후 수행(알람등록) */
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                groupScheduleNotificationHandler.registerScheduleBeforeOneHour(userCode, savedSchedule);
                notifyGroupScheduleCreation(groupRoom, savedSchedule);
            }
        });
        return SendGroupResponse.from(savedSchedule, Action.CREATE);
    }

    public SendGroupResponse updateScheduleByGroupRoom(String userCode, String groupCode, SendUpdateScheduleDTO request) {
        log.info("Updated 그룹스케줄 groupCode: {}, request: {}", groupCode, request);
        checkUserAccessToGroupRoom(groupCode, userCode);

        Schedule schedule = scheduleQueryService.findScheduleById(request.scheduleId());
        schedule.update(request.title(), request.content(), request.startTime(), request.endTime());

        /* 커밋 이후 수행(알람수정) */
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                groupScheduleNotificationHandler.updateScheduleBeforeOneHour(userCode, schedule);
            }
        });

        return SendGroupResponse.from(schedule, Action.UPDATE);
    }

    public SendGroupResponse deleteScheduleByGroupRoom(String userCode, String groupCode, SendDeleteScheduleDTO request) {
        log.info("DELETE 그룹스케줄 groupCode: {}, request: {}", groupCode, request);
        checkUserAccessToGroupRoom(groupCode, userCode);
        Schedule schedule = scheduleQueryService.findScheduleById(request.scheduleId());
        scheduleQueryService.delete(schedule);
        /* 커밋 이후 수행(알람삭제) */
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                groupScheduleNotificationHandler.deleteScheduleBeforeOneHour(request.scheduleId());
            }
        });
        return SendGroupResponse.from(schedule, Action.DELETE);
    }

    @Transactional(readOnly = true)
    public GroupScheduleResponse getGroupScheduleById(UserResponse userResponse, String groupCode, Long scheduleId) {
        checkUserAccessToGroupRoom(groupCode, userResponse.getUserCode());

        GroupRoom groupRoom = groupQueryService.getGroupByCode(groupCode);
        Schedule schedule = scheduleQueryService.findScheduleById(scheduleId);

        List<UserScheduleAttendance> attendances = getUserScheduleAttendances(groupRoom, scheduleId);

        return GroupScheduleResponse.from(schedule, groupRoom.getName(), attendances);
    }

    @Transactional(readOnly = true)
    public List<GroupScheduleResponse> getSchedulesByGroupRoom(String groupCode, UserResponse userResponse) {
        checkUserAccessToGroupRoom(groupCode, userResponse.getUserCode());
        List<Schedule> schedules = scheduleQueryService.findByGroupRoomCode(groupCode);
        GroupRoom groupRoom = groupQueryService.getGroupByCode(groupCode);

        return schedules.stream()
                .map(schedule -> GroupScheduleResponse.from(schedule, groupRoom.getName(), getUserScheduleAttendances(groupRoom, schedule.getId())))
                .collect(Collectors.toList());
    }

    public List<ScheduleResponse> getWeekSchedule(LocalDate startDate, LocalDate endDate, UserResponse userResponse) {
        return scheduleQueryService.findWeeklyGroupScheduleByUser(startDate, endDate, userResponse.getId())
                .stream()
                .map(ScheduleResponse::from)
                .sorted(ScheduleResponse.getComparatorByStartTime())
                .collect(Collectors.toList());
    }

    public List<ScheduleResponse> getWeekScheduleByGroupCode(LocalDate startDate, LocalDate endDate, String groupRoomCode, UserResponse userResponse) {
        return scheduleQueryService.findWeeklyGroupScheduleByGroupCode(startDate, endDate, userResponse.getId(), groupRoomCode)
                .stream()
                .map(ScheduleResponse::from)
                .sorted(ScheduleResponse.getComparatorByStartTime())
                .collect(Collectors.toList());
    }

    public List<ScheduleResponse> getAllScheduleByGroup(LocalDate startDate, LocalDate endDate, UserResponse userResponse) {
        return scheduleQueryService.findAllGroupScheduleByGroupCode(startDate, endDate, userResponse.getId())
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

    private void notifyGroupScheduleCreation(GroupRoom groupRoom, Schedule schedule) {
        List<User> notificationUsers = userGroupQueryService.findUserByIsConnectionFalse(groupRoom.getId());
        notificationUsers.forEach(member -> {
            String userCode = member.getUserCode();
            String groupName = groupRoom.getName();
            String scheduleTitle = schedule.getTitle();
            String url = "/groupRoom/" + groupRoom.getGroupCode() + "/" + schedule.getId();

            // FCM 알림 전송
            fcmService.notifyGroupScheduleCreation(userCode, groupName, scheduleTitle, url);

            // 이벤트 발행
            GroupScheduleCreatedEvent event = new GroupScheduleCreatedEvent(
                    this, userCode, groupName, scheduleTitle, url);
            eventPublisher.publishEvent(event);
        });

    }


}
