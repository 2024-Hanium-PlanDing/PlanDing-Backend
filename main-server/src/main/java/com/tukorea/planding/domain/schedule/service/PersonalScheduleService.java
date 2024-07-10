package com.tukorea.planding.domain.schedule.service;

import com.tukorea.planding.domain.notify.dto.NotificationDTO;
import com.tukorea.planding.domain.notify.service.ScheduleNotificationService;
import com.tukorea.planding.domain.schedule.dto.request.PersonalScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.request.ScheduleRequest;
import com.tukorea.planding.domain.schedule.dto.response.PersonalScheduleResponse;
import com.tukorea.planding.domain.schedule.dto.response.ScheduleResponse;
import com.tukorea.planding.domain.schedule.entity.PersonalSchedule;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import com.tukorea.planding.domain.schedule.entity.ScheduleType;
import com.tukorea.planding.domain.schedule.repository.PersonalScheduleRepository;
import com.tukorea.planding.domain.user.dto.UserInfo;
import com.tukorea.planding.domain.user.entity.User;
import com.tukorea.planding.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonalScheduleService {

    private final ScheduleQueryService scheduleQueryService;
    private final UserQueryService userQueryService;
    private final PersonalScheduleRepository personalScheduleRepository;
    private final ScheduleNotificationService notificationService;


    // 메인페이지
    public List<ScheduleResponse> getAllSchedule(UserInfo userInfo, int weekOffset) {

        LocalDate today = LocalDate.now().plusWeeks(weekOffset);
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));

        List<Schedule> schedules = scheduleQueryService.findByUserAndScheduleDateBetween(userInfo.getId(), startOfWeek, endOfWeek);

        return schedules.stream()
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
    }

    public PersonalScheduleResponse createSchedule(UserInfo userInfo, PersonalScheduleRequest personalScheduleRequest) {
        User user = userQueryService.getUserByUserCode(userInfo.getUserCode());

        // 개인 스케줄 생성
        PersonalSchedule personalSchedule = PersonalSchedule.builder()
                .user(user)
                .build();

        Schedule newSchedule = Schedule.builder()
                .personalSchedule(personalSchedule)
                .title(personalScheduleRequest.title())
                .content(personalScheduleRequest.content())
                .scheduleDate(personalScheduleRequest.scheduleDate())
                .startTime(personalScheduleRequest.startTime())
                .endTime(personalScheduleRequest.endTime())
                .isComplete(false)
                .type(ScheduleType.PERSONAL)
                .build();

        // 개인 스케줄에 스케줄 추가
        personalSchedule.getSchedules().add(newSchedule);
        personalScheduleRepository.save(personalSchedule);
        Schedule schedule = scheduleQueryService.save(newSchedule);

        scheduleNotification(userInfo, personalScheduleRequest, schedule);

        return PersonalScheduleResponse.from(schedule);
    }

    @Transactional(readOnly = true)
    public PersonalScheduleResponse getSchedule(Long scheduleId, UserInfo userInfo) {
        Schedule schedule = scheduleQueryService.findScheduleById(scheduleId);
        schedule.getPersonalSchedule().checkOwnership(userInfo.getId());
        return PersonalScheduleResponse.from(schedule);
    }

    public void deleteSchedule(UserInfo userInfo, Long scheduleId) {
        Schedule schedule = scheduleQueryService.findScheduleById(scheduleId);
        schedule.getPersonalSchedule().checkOwnership(userInfo.getId());
        scheduleQueryService.delete(schedule);
    }


    public PersonalScheduleResponse updateSchedule(Long scheduleId, PersonalScheduleRequest scheduleRequest, UserInfo userInfo) {
        Schedule schedule = scheduleQueryService.findScheduleById(scheduleId);
        schedule.getPersonalSchedule().checkOwnership(userInfo.getId());
        schedule.update(scheduleRequest.title(), scheduleRequest.content(), scheduleRequest.startTime(), scheduleRequest.endTime());
        return PersonalScheduleResponse.from(schedule);
    }

    public List<PersonalScheduleResponse> getWeekSchedule(LocalDate startDate, LocalDate endDate, UserInfo userInfo) {
        return scheduleQueryService.findWeeklyPersonalScheduleByUser(startDate, endDate, userInfo.getId())
                .stream()
                .map(PersonalScheduleResponse::from)
                .sorted(PersonalScheduleResponse.getComparatorByStartTime())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> findOverlapSchedule(Long userId, ScheduleRequest scheduleRequest) {
        return scheduleQueryService.findOverlapSchedule(userId, scheduleRequest);
    }


    private void scheduleNotification(UserInfo userInfo, PersonalScheduleRequest personalScheduleRequest, Schedule schedule) {
        LocalDateTime start = LocalDateTime.of(personalScheduleRequest.scheduleDate(), LocalTime.ofSecondOfDay(personalScheduleRequest.startTime()));
        NotificationDTO notification = NotificationDTO.createPersonalSchedule(userInfo.getUserCode(), personalScheduleRequest.title(), "/api/v1/schedule/" + schedule.getId(), String.valueOf(schedule.getScheduleDate()) + "-" + schedule.getStartTime());
        notificationService.scheduleNotification(notification, start.minusHours(1));
    }
}
