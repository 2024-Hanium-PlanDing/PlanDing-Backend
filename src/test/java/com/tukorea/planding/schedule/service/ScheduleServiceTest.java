package com.tukorea.planding.schedule.service;

import com.tukorea.planding.domain.schedule.entity.ScheduleStatus;
import com.tukorea.planding.domain.schedule.repository.ScheduleRepository;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.dto.ScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.ScheduleResponse;
import com.tukorea.planding.domain.schedule.repository.ScheduleRepositoryCustomImpl;
import com.tukorea.planding.domain.schedule.service.ScheduleService;
import com.tukorea.planding.domain.user.repository.UserRepository;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.global.error.BusinessException;
import com.tukorea.planding.global.error.ErrorCode;
import com.tukorea.planding.global.oauth.details.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ScheduleServiceTest {

    private static final String TEST_EMAIL = "test@";
    private static final String TEST_TITLE = "Test Schedule";
    private static final String TEST_CONTENT = "Test Content";
    private static final LocalDate TEST_DATE = LocalDate.of(2024, 01, 02);
    private static final LocalDate END_DATE = LocalDate.of(2024, 01, 03);


    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScheduleRepositoryCustomImpl scheduleRepositoryCustom;

    @Test
    void createSchedule() {
        //given
        UserInfo userInfo = UserInfo.builder()
                .email("test@")
                .userCode("#test")
                .role(Role.USER)
                .build();

        User user = User.builder()
                .email("test@")
                .userCode("#test")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(10, 0);

        ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                .startTime(startTime)
                .endTime(endTime)
                .title(TEST_TITLE)
                .content(TEST_CONTENT)
                .scheduleDate(TEST_DATE)
                .build();

        //when
        ScheduleResponse schedule = scheduleService.createSchedule(userInfo, scheduleRequest);

        //then
        assertNotNull(schedule);
        assertEquals(TEST_TITLE, schedule.title());
        assertEquals(TEST_CONTENT, schedule.content());
        assertEquals(startTime, schedule.startTime());
        assertEquals(endTime, schedule.endTime());
    }

    @Test
    void deleteSchedule() {
        //given
        UserInfo userInfo = UserInfo.builder()
                .email(TEST_EMAIL)
                .userCode("#CODE")
                .role(Role.USER)
                .build();

        createUserAndSave(TEST_EMAIL);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(10, 0);

        ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                .startTime(startTime)
                .endTime(endTime)
                .title(TEST_TITLE)
                .content(TEST_CONTENT)
                .scheduleDate(TEST_DATE)
                .build();

        //when
        ScheduleResponse schedule = scheduleService.createSchedule(userInfo, scheduleRequest);
        scheduleService.deleteSchedule(userInfo, schedule.id());

        //then
        assertEquals(Optional.empty(), scheduleRepository.findById(schedule.id()));
    }

    @Test
    @DisplayName("주간 개인 스케줄 가져오기")
    public void getWeekSchedule() {
        //given
        UserInfo userInfo = UserInfo.builder()
                .email("test@")
                .role(Role.USER)
                .userCode("#CODE")
                .username("username")
                .build();

        User user = createUserAndSave(TEST_EMAIL);

        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(10, 0);

        Schedule schedule = Schedule
                .builder()
                .user(user)
                .scheduleDate(TEST_DATE)
                .title(TEST_TITLE)
                .content(TEST_CONTENT)
                .startTime(startTime)
                .endTime(endTime)
                .build();

        scheduleRepository.save(schedule);

        Schedule schedule1 = createAndSaveSchedule(user, TEST_TITLE, TEST_CONTENT, startTime, endTime, TEST_DATE);

        scheduleRepository.save(schedule1);

        //when
        List<ScheduleResponse> result = scheduleService.getWeekSchedule(TEST_DATE, END_DATE, userInfo);

        //then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(result.get(0).startTime(), schedule1.getStartTime());
        assertEquals(result.get(1).startTime(), schedule.getStartTime());
    }

    @Test
    @DisplayName("스케줄 수정 테스트")
    public void updateSchedule() {
        //given
        User user = createUserAndSave(TEST_EMAIL);
        UserInfo userInfo = User.toUserInfo(user);

        LocalTime startTime = LocalTime.of(7, 0);
        LocalTime endTime = LocalTime.of(9, 0);
        Schedule schedule = createAndSaveSchedule(user, TEST_TITLE, TEST_CONTENT, startTime, endTime, TEST_DATE);

        //when
        String updateTitle = "update_title";
        String updateContent = "update_content";
        schedule.update(updateTitle, updateContent, null, null);

        //then
        assertEquals(schedule.getTitle(), updateTitle);
        assertEquals(schedule.getContent(), updateContent);
        assertEquals(schedule.getStartTime(), startTime);
        assertEquals(schedule.getEndTime(), endTime);
    }

    @Test
    @DisplayName("실패: 스케줄 수정시 endTime < startTime")
    public void updateError() {
        //given
        User user = createUserAndSave(TEST_EMAIL);

        LocalTime startTime = LocalTime.of(7, 0);
        LocalTime endTime = LocalTime.of(9, 0);
        Schedule schedule = createAndSaveSchedule(user, TEST_TITLE, TEST_CONTENT, startTime, endTime, TEST_DATE);

        assertEquals(startTime, schedule.getStartTime());
        assertEquals(endTime, schedule.getEndTime());
        //when
        String updateTitle = "update_title";
        String updateContent = "update_content";

        //then
        assertThrows(IllegalArgumentException.class, () -> schedule.update(updateTitle, updateContent, LocalTime.of(10, 0), LocalTime.of(9, 10)));
    }

    @Test
    @DisplayName("성공: 스케줄 상태변화")
    public void statusTest() {
        //given
        User user = createUserAndSave(TEST_EMAIL);

        LocalTime startTime = LocalTime.of(7, 0);
        LocalTime endTime = LocalTime.of(9, 0);
        Schedule schedule = createAndSaveSchedule(user, TEST_TITLE, TEST_CONTENT, startTime, endTime, TEST_DATE);

        ScheduleStatus status = ScheduleStatus.POSSIBLE;
        scheduleService.updateScheduleStatus(schedule.getId(), status);

        Schedule result = scheduleRepository.findById(schedule.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND));

        assertEquals(ScheduleStatus.POSSIBLE, result.getStatus());
    }

    @Test
    @DisplayName("성공: 스케줄을 생성할때 겹치는 스케줄 가져오기")
    public void overlapSchedule(){
        User user=createUserAndSave("email");

        LocalTime startTime = LocalTime.of(7, 0);
        LocalTime endTime = LocalTime.of(9, 0);
        createAndSaveSchedule(user, TEST_TITLE, TEST_CONTENT, startTime, endTime, TEST_DATE);

        LocalTime startTime2 = LocalTime.of(9, 0);
        LocalTime endTime2 = LocalTime.of(11, 0);
        createAndSaveSchedule(user, TEST_TITLE, TEST_CONTENT, startTime2, endTime2, TEST_DATE);

        LocalTime startTime3 = LocalTime.of(9, 0);
        LocalTime endTime3 = LocalTime.of(9, 30);
        createAndSaveSchedule(user, TEST_TITLE, TEST_CONTENT, startTime3, endTime3, TEST_DATE);

        List<Schedule> result = scheduleRepositoryCustom.findOverlapSchedules(user.getId(), TEST_DATE, LocalTime.of(8, 0), LocalTime.of(10, 0));
        assertThat(result).isNotEmpty();
        assertEquals(3,result.size());


    }

    private User createUserAndSave(String email) {
        User user = User.builder()
                .email(email)
                .role(Role.USER)
                .userCode("#CODE")
                .username("username").build();
        return userRepository.save(user);
    }

    private Schedule createAndSaveSchedule(User user, String title, String content, LocalTime startTime, LocalTime endTime, LocalDate date) {
        Schedule schedule = Schedule.builder()
                .user(user)
                .scheduleDate(date)
                .title(title)
                .content(content)
                .startTime(startTime)
                .endTime(endTime)
                .build();
        return scheduleRepository.save(schedule);
    }

}