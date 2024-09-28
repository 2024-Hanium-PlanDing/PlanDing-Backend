package com.tukorea.planding.domain.schedule.repository;

import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.user.dto.UserInfo;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepositoryCustom {
    /**
     * 부터 까지 작성한 스케줄 목록 조회
     *
     * @param startDate 부터 검색할 날짜
     * @param endDate   까지 검색할 날짜
     * @return List<Schedule>
     */
    List<Schedule> findWeeklyScheduleByUser(LocalDate startDate, LocalDate endDate, Long userId);

    List<Schedule> findWeeklyPersonalScheduleByUser(LocalDate startDate, LocalDate endDate, Long userId);

    List<Schedule> findWeeklyGroupScheduleByUser(LocalDate startDate, LocalDate endDate, Long userId);

    List<Schedule> findWeeklyGroupScheduleByGroupCode(LocalDate startDate, LocalDate endDate, Long userId, String groupRoomCode);

    /**
     * 스케줄을 작성할때 작성한 스케줄과 겹치는 스케줄 조회
     * 총 3가지 경우로 나눠볼 수 있다.
     * <p>
     * ex) startTime: 8시, endTime: 10시 의 스케줄이 저장되어있을 때
     * <p>
     * 추가하려는 스케줄이
     * 1. startTime: 7시, endTime: 9시
     * 2. startTime: 9시, endTime: 11시
     * 3. startTime: 9시, endTime: 9시 30분
     *
     * @param userId    스케줄 작성한 유저
     * @param date      작성한 스케줄의 날짜
     * @param startDate 작성한 스케줄의 시작시간
     * @param endDate   작성한 스케줄의 끝나는 시간
     * @return 중복되는 Schedule
     */
    List<Schedule> findOverlapSchedules(Long userId, LocalDate date, Integer startDate, Integer endDate);

    List<Schedule> showTodaySchedule(Long userId);

    List<Schedule> findAllGroupScheduleByGroupCode(LocalDate startDate, LocalDate endDate, Long userId);

    // chatbot
    List<Schedule> showTodaySchedule(String identity);


    List<Schedule> findByGroupRoomCode(String groupCode);

    List<Schedule> findByUserAndScheduleDateBetween(Long userId, LocalDate startOfWeek, LocalDate endOfWeek);
}
