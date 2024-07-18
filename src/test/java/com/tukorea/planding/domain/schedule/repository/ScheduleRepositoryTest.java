//package com.tukorea.planding.domain.schedule.repository;
//
//import com.tukorea.planding.domain.schedule.entity.PersonalSchedule;
//import com.tukorea.planding.domain.schedule.entity.Schedule;
//import com.tukorea.planding.domain.schedule.entity.ScheduleType;
//import com.tukorea.planding.domain.schedule.repository.PersonalScheduleRepository;
//import com.tukorea.planding.domain.schedule.repository.ScheduleRepository;
//import com.tukorea.planding.domain.user.entity.User;
//import com.tukorea.planding.domain.user.repository.UserRepository;
//import com.tukorea.planding.global.config.QueryDslConfig;
//import com.tukorea.planding.global.oauth.details.Role;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Transactional
//@Import(QueryDslConfig.class)
//public class ScheduleRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private ScheduleRepository scheduleRepository;
//    @Autowired
//    private PersonalScheduleRepository personalScheduleRepository;
//
//    private User testUser;
//
//    @BeforeEach
//    public void setUp() {
//        testUser = userRepository.save(User.builder()
//                .userCode("#test")
//                .email("email")
//                .role(Role.USER)
//                .build());
//    }
//
//    private Schedule createAndSaveSchedule(LocalTime startTime, LocalTime endTime) {
//        PersonalSchedule personalSchedule = personalScheduleRepository.save(PersonalSchedule.builder()
//                .user(testUser)
//                .build());
//        return Schedule.builder()
//                .scheduleDate(LocalDate.now())
//                .title("title")
//                .content("content")
//                .startTime(startTime)
//                .endTime(endTime)
//                .personalSchedule(personalSchedule)
//                .type(ScheduleType.PERSONAL)
//                .build();
//    }
//
//    @Test
//    public void 겹치는스케줄_가져오기() {
//
//        List<Schedule> schedules = new ArrayList<>();
//
//        LocalTime startTime = LocalTime.of(7, 0);
//        LocalTime endTime = LocalTime.of(9, 0);
//        Schedule schedule1 = createAndSaveSchedule(startTime, endTime);
//
//        LocalTime startTime2 = LocalTime.of(9, 0);
//        LocalTime endTime2 = LocalTime.of(11, 0);
//        Schedule schedule2 = createAndSaveSchedule(startTime2, endTime2);
//
//
//        LocalTime startTime3 = LocalTime.of(9, 0);
//        LocalTime endTime3 = LocalTime.of(9, 30);
//        Schedule schedule3 = createAndSaveSchedule(startTime3, endTime3);
//
//        schedules.add(schedule1);
//        schedules.add(schedule2);
//        schedules.add(schedule3);
//
//        scheduleRepository.saveAll(schedules);
//
//        // when
//        List<Schedule> result = scheduleRepository.findOverlapSchedules(testUser.getId(), LocalDate.now(), 8, 10);
//
//        // then
//        assertThat(result).isNotEmpty();
//        assertThat(result.size()).isEqualTo(3);
//    }
//
//    @Test
//    public void 오늘_스케줄_가져오기() {
//        Schedule schedule=createAndSaveSchedule(LocalTime.now(), LocalTime.now().plusHours(1));
//        scheduleRepository.save(schedule);
//
//        List<Schedule> result = scheduleRepository.showTodaySchedule(testUser.getId());
//
//        assertThat(result).isNotEmpty();
//        assertThat(result.size()).isEqualTo(1);
//    }
//
//}
