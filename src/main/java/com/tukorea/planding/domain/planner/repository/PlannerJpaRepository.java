package com.tukorea.planding.domain.planner.repository;

import com.tukorea.planding.domain.planner.entity.Planner;
import com.tukorea.planding.domain.schedule.entity.Schedule;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PlannerJpaRepository extends JpaRepository<Planner, Long>, PlannerRepositoryCustom {

    List<Planner> findBySchedule(Schedule schedule);

    @Query("SELECT p FROM Planner p " +
            "JOIN p.schedule s " +
            "JOIN s.groupSchedule g " +
            "WHERE g.groupRoom.groupCode =:groupCode " +
            "AND p.schedule.scheduleDate between :startDate and :endDate"
    )
    List<Planner> findAllByGroupAndDateRange(@Param("groupCode") String groupCode,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);
}
