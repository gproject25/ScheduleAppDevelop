package com.scheduleappdevelop.schedule.repository;

import com.scheduleappdevelop.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    List<Schedule> findSchedulesByUserId(Long userId);
}
