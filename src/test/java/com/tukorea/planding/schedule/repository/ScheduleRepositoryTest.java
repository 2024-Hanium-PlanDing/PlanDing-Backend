//package com.tukorea.planding.schedule.repository;
//
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import com.tukorea.planding.domain.schedule.entity.Schedule;
//import com.tukorea.planding.domain.schedule.repository.ScheduleRepository;
//import com.tukorea.planding.domain.schedule.repository.ScheduleRepositoryCustom;
//import com.tukorea.planding.domain.schedule.repository.ScheduleRepositoryCustomImpl;
//import com.tukorea.planding.domain.userCodes.entity.User;
//import com.tukorea.planding.domain.userCodes.repository.UserRepository;
//import com.tukorea.planding.global.oauth.details.Role;
//import jakarta.persistence.EntityManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.TestConstructor;
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
//@Transactional
//@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class ScheduleRepositoryTest {
//
//    private final ScheduleRepositoryCustom repository;
//    private final UserRepository userRepository;
//    private final ScheduleRepository scheduleRepository;
//    private User testUser;
//
//    public ScheduleRepositoryTest(EntityManager entityManager, UserRepository userRepository, ScheduleRepository scheduleRepository) {
//        this.repository = new ScheduleRepositoryCustomImpl(new JPAQueryFactory(entityManager));
//        this.userRepository = userRepository;
//        this.scheduleRepository = scheduleRepository;
//    }
//
//
//    @BeforeEach
//    public void setUp() {
//        final String email = "test@google.com";
//        final String userCode = "#test";
//
//        User userCodes = User.builder()
//                .userCode(userCode)
//                .email(email)
//                .role(Role.USER)
//                .build();
//
//        testUser = userRepository.save(userCodes);
//    }
//
//    private Schedule createAndSaveSchedule(User userCodes, LocalTime startTime, LocalTime endTime) {
//        return Schedule.builder()
//                .userCodes(userCodes)
//                .scheduleDate(LocalDate.now())
//                .title("title")
//                .content("content")
//                .startTime(startTime)
//                .endTime(endTime)
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
//        Schedule schedule1 = createAndSaveSchedule(testUser, startTime, endTime);
//
//        LocalTime startTime2 = LocalTime.of(9, 0);
//        LocalTime endTime2 = LocalTime.of(11, 0);
//        Schedule schedule2 = createAndSaveSchedule(testUser, startTime2, endTime2);
//
//
//        LocalTime startTime3 = LocalTime.of(9, 0);
//        LocalTime endTime3 = LocalTime.of(9, 30);
//        Schedule schedule3 = createAndSaveSchedule(testUser, startTime3, endTime3);
//
//        schedules.add(schedule1);
//        schedules.add(schedule2);
//        schedules.add(schedule3);
//
//        scheduleRepository.saveAll(schedules);
//
//        // when
//        List<Schedule> result = repository.findOverlapSchedules(testUser.getId(), LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(10, 0));
//
//        // then
//        assertThat(result).isNotEmpty();
//        assertThat(result.size()).isEqualTo(3);
//    }
//
//}
