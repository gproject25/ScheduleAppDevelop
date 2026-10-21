package com.scheduleappdevelop.schedule.repository;

import com.scheduleappdevelop.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    List<Schedule> findSchedulesByUserId(Long userId);

    @Query("SELECT S FROM Schedule S JOIN S.user u WHERE u.deleted = false AND (:userId IS NULL OR u.id = :userId)")
    List<Schedule> findAllByActiveUser(@Param("userId") Long userId);

    @Query("SELECT S FROM Schedule S JOIN S.user u WHERE u.deleted = false AND S.id = :id")
    Optional<Schedule> findByIdAndActiveUser(@Param("id") Long id);
}
