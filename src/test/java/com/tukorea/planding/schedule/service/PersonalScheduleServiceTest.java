//package com.tukorea.planding.schedule.service;
//
//import com.tukorea.planding.domain.schedule.dto.request.ScheduleRequest;
//import com.tukorea.planding.domain.schedule.dto.response.PersonalScheduleResponse;
//import com.tukorea.planding.domain.schedule.dto.response.ScheduleResponse;
//import com.tukorea.planding.domain.schedule.entity.PersonalSchedule;
//import com.tukorea.planding.domain.schedule.entity.Schedule;
//import com.tukorea.planding.domain.schedule.repository.PersonalScheduleRepository;
//import com.tukorea.planding.domain.schedule.repository.ScheduleRepository;
//import com.tukorea.planding.domain.schedule.repository.ScheduleRepositoryCustomImpl;
//import com.tukorea.planding.domain.schedule.service.ScheduleQueryService;
//import com.tukorea.planding.domain.schedule.service.PersonalScheduleService;
//import com.tukorea.planding.domain.user.entity.SocialType;
//import com.tukorea.planding.domain.user.entity.User;
//import com.tukorea.planding.domain.user.mapper.UserMapper;
//import com.tukorea.planding.domain.user.service.UserQueryService;
//import com.tukorea.planding.global.oauth.details.Role;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//
//@ExtendWith(MockitoExtension.class)
//@Transactional
//class PersonalScheduleServiceTest {
//
//
//    @InjectMocks
//    PersonalScheduleService personalScheduleService;
//
//    @Mock
//    UserQueryService userQueryService;
//
//    @Mock
//    ScheduleQueryService scheduleQueryService;
//
//    @Mock
//    ScheduleRepository scheduleRepository;
//
//    @Mock
//    PersonalScheduleRepository personalScheduleRepository;
//
//
//    private User testUser;
//    private Schedule schedule;
//    private PersonalSchedule personalSchedule;
//    private ScheduleRequest scheduleRequest;
//
//
//    @BeforeEach
//    void setUp() {
//        testUser = new User("test", "profile", "username", Role.USER, SocialType.KAKAO, null, "#test"); // 테스트용 사용자 정보 초기화
//        personalSchedule = new PersonalSchedule(testUser);
//        schedule = new Schedule("title", "content", LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0), true, personalSchedule, null);
//        scheduleRequest = new ScheduleRequest("#test", "title", "content", LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0));
//    }
//
//
//    @Test
//    void create_스케줄_생성() {
//        // given
//        when(scheduleQueryService.save(any(Schedule.class))).thenReturn(schedule);
//        when(personalScheduleRepository.save(any(PersonalSchedule.class))).thenReturn(personalSchedule);
//
//        // when
//        PersonalScheduleResponse result = personalScheduleService.createSchedule(UserMapper.toUserInfo(testUser), scheduleRequest);
//
//        // then
//        assertNotNull(result);
//        assertEquals("title", result.title());
//        assertEquals("content", result.content());
//        assertEquals(LocalTime.of(9, 0), result.startTime());
//        assertEquals(LocalTime.of(10, 0), result.endTime());
//    }
//
//    @Test
//    void delete_스케줄_삭제() {
//        //given
//        when(scheduleQueryService.findScheduleById(1L)).thenReturn(schedule);
//        doNothing().when(personalSchedule).checkOwnership(1L);
//
//        //when
//        personalScheduleService.deleteSchedule(UserMapper.toUserInfo(testUser), 1L);
//
//        //then
//        verify(scheduleQueryService).delete(schedule);
//    }
//
////    @Test
////    public void 주간개인스케줄_가져오기() {
////        //given
////        List<Schedule> responses = Collections.singletonList(schedule);
////        when(userQueryService.getUserByUserCode(any())).thenReturn(testUser);
////        when(scheduleRepository.findWeeklyScheduleByUser(any(), any(), eq(testUser.getId()))).thenReturn(responses);
////
////        //when
////        List<PersonalScheduleResponse> result = personalScheduleService.getWeekSchedule(LocalDate.now(), LocalDate.now().plusDays(7), UserMapper.toUserInfo(testUser));
////
////        //then
////        assertNotNull(result);
////        assertEquals(1, result.size());
////        assertEquals(result.get(0).startTime(), schedule.getStartTime());
////        assertEquals(result.get(0).endTime(), schedule.getEndTime());
////    }
//
//    @Test
//    public void update_스케줄수정() {
//        //when
//        String updateTitle = "update_title";
//        String updateContent = "update_content";
//        schedule.update(updateTitle, updateContent, null, null);
//
//        //then
//        assertEquals(schedule.getTitle(), updateTitle);
//        assertEquals(schedule.getContent(), updateContent);
//    }
//
//    @Test
//    public void update_스케줄수정시_endTime이startTime보다작을때() {
//        //given
//        assertThrows(IllegalArgumentException.class, () -> schedule.update(null, null, LocalTime.of(10, 0), LocalTime.of(9, 10)));
//    }
//}