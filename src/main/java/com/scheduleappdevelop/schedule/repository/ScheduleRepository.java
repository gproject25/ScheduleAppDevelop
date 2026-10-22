package com.scheduleappdevelop.schedule.repository;

import com.scheduleappdevelop.schedule.dto.SchedulePageResponse;
import com.scheduleappdevelop.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    @Query("SELECT S FROM Schedule S JOIN S.user u WHERE S.deleted = false AND (:userId IS NULL OR u.id = :userId)")
    List<Schedule> findAllByActiveSchedule(@Param("userId") Long userId);

    Optional<Schedule> findByIdAndDeletedFalse(Long scheduleId);


    //Paging
    @Query("""
    SELECT new com.scheduleappdevelop.schedule.dto.SchedulePageResponse(
        s.id,
        u.name,
        s.title,
        s.content,
        (SELECT COUNT(c) FROM Comment c WHERE c.schedule.id = s.id),
        s.createdAt,
        s.modifiedAt
    )
    FROM Schedule s
    JOIN s.user u
    """)
    Page<SchedulePageResponse> getSchedulesByPage(Pageable pageable);

}
