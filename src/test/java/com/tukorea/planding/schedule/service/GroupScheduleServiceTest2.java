//package com.tukorea.planding.schedule.service;
//
//import com.tukorea.planding.domain.group.entity.GroupRoom;
//import com.tukorea.planding.domain.group.service.query.GroupQueryService;
//import com.tukorea.planding.domain.group.service.query.UserGroupQueryService;
//import com.tukorea.planding.domain.schedule.dto.request.ScheduleRequest;
//import com.tukorea.planding.domain.schedule.dto.response.ScheduleResponse;
//import com.tukorea.planding.domain.schedule.entity.GroupSchedule;
//import com.tukorea.planding.domain.schedule.entity.Schedule;
//import com.tukorea.planding.domain.schedule.entity.ScheduleType;
//import com.tukorea.planding.domain.schedule.repository.GroupScheduleRepository;
//import com.tukorea.planding.domain.schedule.service.GroupScheduleService;
//import com.tukorea.planding.domain.schedule.service.ScheduleQueryService;
//import com.tukorea.planding.domain.user.entity.SocialType;
//import com.tukorea.planding.domain.user.entity.User;
//import com.tukorea.planding.domain.user.service.UserQueryService;
//import com.tukorea.planding.global.oauth.details.Role;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.ArrayList;
//
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@Transactional
//public class GroupScheduleServiceTest2 {
//
//    @InjectMocks
//    private GroupScheduleService groupScheduleService;
//
//    @Mock
//    private ScheduleQueryService scheduleQueryService;
//
//    @Mock
//    private GroupQueryService groupQueryService;
//
//    @Mock
//    private UserQueryService userQueryService;
//
//    @Mock
//    private UserGroupQueryService userGroupQueryService;
//
//    @Mock
//    private GroupScheduleRepository groupScheduleRepository;
//
//    @Mock
//    private ApplicationEventPublisher eventPublisher;
//
//    private User user;
//    private GroupRoom groupRoom;
//    private ScheduleRequest scheduleRequest;
//    private GroupSchedule groupSchedule;
//    private Schedule schedule;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//
//        user = User.builder()
//                .userCode("testUser")
//                .build();
//
//        groupRoom = GroupRoom.builder()
//                .name("testGroup")
//                .build();
//
//
//        scheduleRequest = new ScheduleRequest(1L, "testTitle", "testContent", LocalDate.now(), LocalTime.now(), LocalTime.now().plusHours(1));
//
//        schedule = Schedule.builder()
//                .title("testTitle")
//                .content("testContent")
//                .scheduleDate(LocalDate.now())
//                .startTime(LocalTime.now())
//                .endTime(LocalTime.now().plusHours(1))
//                .isComplete(false)
//                .type(ScheduleType.GROUP)
//                .build();
//    }
//
//    @Test
//    public void 그룹스케줄_생성_성공() {
//        when(userQueryService.getUserByUserCode("testUser")).thenReturn(user);
//        when(groupQueryService.getGroupByCode("testGroup")).thenReturn(groupRoom);
//        when(scheduleQueryService.save(any(Schedule.class))).thenReturn(schedule);
//        when(userGroupQueryService.findUserByIsConnectionFalse(anyLong())).thenReturn(new ArrayList<>());
//
//
//        doNothing().when(userGroupQueryService).checkUserAccessToGroupRoom(anyLong(), any());
//
//        ScheduleResponse response = groupScheduleService.createGroupSchedule("testGroup", scheduleRequest);
//
//        assertEquals("testTitle", response.title());
//        assertEquals("testContent", response.content());
//        verify(groupScheduleRepository, times(1)).save(any(GroupSchedule.class));
//        verify(scheduleQueryService, times(1)).save(any(Schedule.class));
//        verify(eventPublisher, times(0)).publishEvent(any());
//    }
//}
