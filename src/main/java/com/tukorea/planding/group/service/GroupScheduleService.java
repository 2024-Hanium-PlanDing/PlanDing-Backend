package com.tukorea.planding.group.service;

import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import com.tukorea.planding.group.dao.GroupRoomRepository;
import com.tukorea.planding.group.domain.GroupRoom;
import com.tukorea.planding.schedule.dao.ScheduleRepository;
import com.tukorea.planding.schedule.domain.Schedule;
import com.tukorea.planding.schedule.dto.RequestSchedule;
import com.tukorea.planding.schedule.dto.ResponseSchedule;
import com.tukorea.planding.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupRoomRepository groupRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseSchedule createGroupSchedule(String groupCode, RequestSchedule requestSchedule) {

        GroupRoom groupRoom = groupRoomRepository.findByGroupCode(groupCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_ROOM_NOT_FOUND));

        Schedule schedule = Schedule.builder()
                .title(requestSchedule.getTitle())
                .content(requestSchedule.getContent())
                .scheduleDate(requestSchedule.getDate())
                .startTime(requestSchedule.getStartTime())
                .endTime(requestSchedule.getEndTime())
                .isComplete(false)
                .groupRoom(groupRoom)
                .build();

        groupRoom.addSchedule(schedule);
        // 그룹 스케줄을 데이터베이스에 저장
        Schedule save = scheduleRepository.save(schedule);

        return ResponseSchedule.from(save);
    }
}
