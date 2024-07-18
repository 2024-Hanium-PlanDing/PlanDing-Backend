package com.tukorea.planding.domain.schedule.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.tukorea.planding.domain.group.entity.GroupRoom;
import com.tukorea.planding.domain.group.service.query.GroupQueryService;
import com.tukorea.planding.domain.group.service.query.UserGroupQueryService;
import com.tukorea.planding.domain.schedule.dto.request.ScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.response.GroupScheduleResponse;
import com.tukorea.planding.domain.schedule.dto.response.ScheduleResponse;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleType;
import com.tukorea.planding.domain.schedule.entity.GroupSchedule;
import com.tukorea.planding.domain.user.dto.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;

@ExtendWith(MockitoExtension.class)
public class GroupScheduleServiceUnitTest {

    @InjectMocks
    private GroupScheduleService groupScheduleService;

    @Mock
    private ScheduleQueryService scheduleQueryService;

    @Mock
    private GroupQueryService groupQueryService;

    @Mock
    private UserGroupQueryService userGroupQueryService;

    private GroupRoom mockGroupRoom;
    private ScheduleRequest mockRequest;
    private Schedule mockSavedSchedule;
    private GroupSchedule mockGroupSchedule;
    private UserInfo mockUserInfo;

    @BeforeEach
    public void setup() {
        mockRequest = ScheduleRequest.builder()
                .userId(1L)
                .title("testTitle")
                .content("testContent")
                .scheduleDate(LocalDate.now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now().plusHours(1))
                .build();

        mockGroupRoom = GroupRoom.builder()
                .name("testGroup")
                .description("test")
                .owner("testUser")
                .groupCode("groupCode")
                .build();

        ReflectionTestUtils.setField(mockGroupRoom, "id", 1L);

        mockGroupSchedule = GroupSchedule.builder()
                .groupRoom(mockGroupRoom)
                .build();

        mockSavedSchedule = Schedule.builder()
                .title("testTitle")
                .content("testContent")
                .scheduleDate(LocalDate.now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now().plusHours(1))
                .isComplete(false)
                .type(ScheduleType.GROUP)
                .groupSchedule(mockGroupSchedule)
                .build();

        ReflectionTestUtils.setField(mockSavedSchedule, "id", 1L);


        mockUserInfo = UserInfo.builder()
                .id(1L)
                .username("testUser")
                .build();

        given(userGroupQueryService.checkUserAccessToGroupRoom(anyLong(), any())).willReturn(true);
    }

    @Test
    @DisplayName("그룹스케줄을 생성한다.")
    public void createGroupSchedule_Success() {
        // given
        given(groupQueryService.getGroupByCode(any(String.class))).willReturn(mockGroupRoom);
        given(scheduleQueryService.save(any(Schedule.class))).willReturn(mockSavedSchedule);

        // when
        ScheduleResponse result = groupScheduleService.createGroupSchedule("groupCode", mockRequest);

        // then
        assertNotNull(result);
        assertEquals("testTitle", result.title());
        assertEquals("testContent", result.content());
        verify(groupQueryService).getGroupByCode("groupCode");
        verify(scheduleQueryService).save(any(Schedule.class));
    }

    @Test
    @DisplayName("그룹스케줄을 조회한다")
    public void showGroupSchedule_Success() {
        given(groupQueryService.getGroupById(anyLong())).willReturn(mockGroupRoom);
        given(scheduleQueryService.findScheduleById(anyLong())).willReturn(mockSavedSchedule);

        // when
        GroupScheduleResponse result = groupScheduleService.getGroupScheduleById(mockUserInfo, 1L, 1L);

        // then
        assertNotNull(result);
        assertEquals("testGroup", result.groupName());
        assertEquals("testTitle", result.title());

        verify(groupQueryService).getGroupById(1L);
        verify(scheduleQueryService).findScheduleById(1L);
    }
}
