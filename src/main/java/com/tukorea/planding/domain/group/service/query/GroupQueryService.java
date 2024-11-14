package com.tukorea.planding.domain.group.service.query;

import com.tukorea.planding.domain.group.entity.domain.GroupRoomDomain;
import com.tukorea.planding.domain.group.repository.normal.GroupRoomRepository;
import com.tukorea.planding.domain.group.repository.usergroup.UserGroupRepository;
import com.tukorea.planding.domain.schedule.entity.domain.GroupScheduleDomain;
import com.tukorea.planding.domain.schedule.repository.port.GroupScheduleRepository;
import com.tukorea.planding.domain.schedule.service.ScheduleQueryService;
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

    public GroupRoomDomain createGroup(GroupRoomDomain groupRoom) {
        return groupRoomRepository.save(groupRoom);
    }

    public List<GroupRoomDomain> findGroupsByUserId(Long userId, PageRequest request) {
        return groupRoomRepository.findGroupRoomsByUserId(userId, request);
    }

    public GroupRoomDomain getGroupById(Long groupId) {
        return groupRoomRepository.findByGroupId(groupId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_ROOM_NOT_FOUND));
    }

    public GroupRoomDomain getGroupByCode(String groupCode) {
        return groupRoomRepository.findByGroupCode(groupCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_ROOM_NOT_FOUND));
    }

    public GroupRoomDomain save(GroupRoomDomain groupRoomDomain) {
        return groupRoomRepository.save(groupRoomDomain);
    }


    public void delete(GroupRoomDomain groupRoom) {
        List<GroupScheduleDomain> schedules = groupScheduleRepository.findAllByGroupRoomId(groupRoom.getId());
        for (GroupScheduleDomain schedule : schedules) {
            schedule.getSchedules().stream()
                    .forEach(s -> scheduleQueryService.deleteGroupScheduleById(s.getId()));
        }
        groupRoomRepository.delete(groupRoom);
    }

    public boolean existsByGroupCode(String groupCode) {
        return groupRoomRepository.existsByGroupCode(groupCode);
    }

    public boolean existGroupInUser(String groupCode, String userCode) {
        return userGroupRepository.existsByGroupRoomAndUser(groupCode, userCode);
    }

}
