package com.scheduleappdevelop.schedule.repository;

import com.scheduleappdevelop.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    @Query("SELECT S FROM Schedule S JOIN S.user u WHERE S.deleted = false AND (:userId IS NULL OR u.id = :userId)")
    List<Schedule> findAllByActiveSchedule(@Param("userId") Long userId);

    Optional<Schedule> findByIdAndDeletedFalse(Long scheduleId);
}
