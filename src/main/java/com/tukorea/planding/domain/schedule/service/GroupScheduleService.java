package com.tukorea.planding.domain.schedule.service;

import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.notify.service.GroupScheduleNotificationService;
import com.tukorea.planding.domain.schedule.dto.request.websocket.SendCreateScheduleDTO;
import com.tukorea.planding.domain.schedule.dto.request.websocket.SendDeleteScheduleDTO;
import com.tukorea.planding.domain.schedule.dto.request.websocket.SendUpdateScheduleDTO;
import com.tukorea.planding.domain.schedule.dto.response.websocket.SendGroupResponse;
import com.tukorea.planding.domain.schedule.entity.Action;
import com.tukorea.planding.domain.schedule.entity.GroupSchedule;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.repository.GroupScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class GroupScheduleService {

    private final ScheduleQueryService scheduleQueryService;
    private final GroupQueryService groupQueryService;
    private final GroupScheduleNotificationService groupScheduleNotificationService;
    private final GroupScheduleRepository groupScheduleRepository;


    public SendGroupResponse createGroupSchedule(String userCode, String groupCode, SendCreateScheduleDTO request) {
        log.info("Create 그룹스케줄 groupCode: {}, request: {}", groupCode, request);
        GroupRoom groupRoom = groupQueryService.getGroupByCode(groupCode);

        GroupSchedule groupSchedule = GroupSchedule.builder()
                .groupRoom(groupRoom)
                .build();

        Schedule newSchedule = Schedule.createGroupSchedule(request, groupSchedule);

        groupSchedule.getSchedules().add(newSchedule);
        groupScheduleRepository.save(groupSchedule);
        Schedule savedSchedule = scheduleQueryService.save(newSchedule);

        /* 커밋 이후 수행(알람등록) */
        groupScheduleNotificationService.registerGroupScheduleNotification(userCode, groupRoom, savedSchedule, Action.CREATE);
        return SendGroupResponse.from(savedSchedule, Action.CREATE);
    }

    public SendGroupResponse updateScheduleByGroupRoom(String userCode, String groupCode, SendUpdateScheduleDTO request) {
        log.info("Updated 그룹스케줄 groupCode: {}, request: {}", groupCode, request);

        Schedule schedule = scheduleQueryService.findScheduleById(request.scheduleId());
        schedule.update(request.title(), request.content(), request.startTime(), request.endTime());

        /* 커밋 이후 수행(알람수정) */
        groupScheduleNotificationService.registerGroupScheduleNotification(userCode, null, schedule, Action.UPDATE);
        return SendGroupResponse.from(schedule, Action.UPDATE);
    }

    public SendGroupResponse deleteScheduleByGroupRoom(String userCode, String groupCode, SendDeleteScheduleDTO request) {
        log.info("DELETE 그룹스케줄 groupCode: {}, request: {}", groupCode, request);
        Schedule schedule = scheduleQueryService.findScheduleById(request.scheduleId());
        scheduleQueryService.delete(schedule);

        /* 커밋 이후 수행(알람삭제) */
        groupScheduleNotificationService.registerGroupScheduleNotification(userCode, null, schedule, Action.DELETE);
        return SendGroupResponse.from(schedule, Action.DELETE);
    }
}
