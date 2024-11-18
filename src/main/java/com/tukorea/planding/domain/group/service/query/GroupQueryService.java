package com.tukorea.planding.domain.group.service.query;

import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.repository.normal.GroupRoomRepository;
import com.tukorea.planding.domain.group.repository.usergroup.UserGroupRepository;
import com.tukorea.planding.domain.schedule.entity.GroupSchedule;
import com.tukorea.planding.domain.schedule.repository.GroupScheduleRepository;
import com.tukorea.planding.domain.schedule.service.ScheduleQueryService;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupQueryService {

    private final GroupRoomRepository groupRoomRepository;
    private final GroupScheduleRepository groupScheduleRepository;
    private final UserGroupRepository userGroupRepository;
    private final ScheduleQueryService scheduleQueryService;

    public GroupRoom createGroup(GroupRoom groupRoom) {
        return groupRoomRepository.save(groupRoom);
    }

    public List<GroupRoom> findGroupsByUserId(Long userId, PageRequest request) {
        return groupRoomRepository.findGroupRoomsByUserId(userId, request);
    }

    public GroupRoom getGroupById(Long groupId) {
        return groupRoomRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_ROOM_NOT_FOUND));
    }

    public GroupRoom getGroupByCode(String groupCode) {
        return groupRoomRepository.findByGroupCode(groupCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_ROOM_NOT_FOUND));
    }


    public void delete(GroupRoom groupRoom) {
        List<GroupSchedule> schedules = groupScheduleRepository.findAllByGroupRoomId(groupRoom.getId());
        for (GroupSchedule schedule : schedules) {
            schedule.getSchedules().stream()
                    .forEach(s -> scheduleQueryService.deleteGroupScheduleById(s.getId()));
        }
        groupRoomRepository.delete(groupRoom);
    }

    public boolean existsByGroupCode(String groupCode) {
        return groupRoomRepository.existsByGroupCode(groupCode);
    }

    public List<User> getGroupUsers(String groupCode) {
        return groupRoomRepository.getGroupUsers(groupCode);
    }

    public boolean existGroupInUser(String groupCode, String userCode) {
        return userGroupRepository.existsByGroupRoomAndUser(groupCode, userCode);
    }

}
